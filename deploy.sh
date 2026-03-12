set -e
cd ~/StoreRader-java
sudo docker-compose pull

# 현재 실행 중인 백엔드 확인 (blue가 켜져있는지 확인)
EXIST_BLUE=$(sudo docker ps -q -f name=con-storerader-back-blue)

if [ -z "$EXIST_BLUE" ]; then
    TARGET_ENV="blue"
    OLD_ENV="green"
    TARGET_PORT=8081
else
    TARGET_ENV="green"
    OLD_ENV="blue"
    TARGET_PORT=8082
fi

# 1. 새 버전의 백엔드 컨테이너 실행
sudo docker-compose up -d backend-${TARGET_ENV} frontend

# 2. Spring Boot가 완전히 켜질 때까지 대기 (Health Check)
for RETRY_COUNT in {1..20}
do
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:${TARGET_PORT}/api || true)

    if [ "$RESPONSE_CODE" != "000" ] && [ -n "$RESPONSE_CODE" ]; then
        break
    fi

    if [ $RETRY_COUNT -eq 20 ]; then
        sudo docker-compose stop backend-${TARGET_ENV}
        exit 1
    fi
    
    sleep 3
done
 
# 3. Nginx가 바라보는 방향 변경
mkdir -p ./nginx-config
echo "upstream backend { server con-storerader-back-${TARGET_ENV}:8080; }" > ./nginx-config/upstream.conf

# 4. Nginx Reload
sudo docker exec con-storerader-front nginx -s reload

# 5. 기존 버전 종료
sudo docker-compose stop backend-${OLD_ENV} || true

# 6. 더미 이미지 정리
sudo docker image prune -f