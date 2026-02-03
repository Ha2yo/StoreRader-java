CREATE TABLE stores(
    -- 식별 번호
    id SERIAL PRIMARY KEY,

    -- 매장 번호
    store_id INTEGER NOT NULL,
    -- 매장 이름
    store_name VARCHAR(100) NOT NULL,
    -- 전화번호
    tel_no VARCHAR(20),
    -- 우편번호
    post_no VARCHAR(10),
    -- 지번 주소
    jibun_addr VARCHAR(255),
    -- 도로명 주소
    road_addr VARCHAR(255),
    -- 위도 좌표
    lat double precision,
    -- 경도 좌표
    lng double precision,
    -- 지역 코드
    area_code VARCHAR(10),
    -- 지역 상세 코드
    area_detail_code VARCHAR(10),

    -- 생성 일자
    created_at TIMESTAMPTZ DEFAULT NOW(),
    -- 수정 일자
    updated_at TIMESTAMPTZ DEFAULT NOW(),
);