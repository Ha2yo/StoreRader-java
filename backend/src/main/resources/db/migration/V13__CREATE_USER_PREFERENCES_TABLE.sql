CREATE TABLE user_preferences (
    -- users 테이블의 식별 번호
    id INTEGER PRIMARY KEY,

    -- 해당 유저가 매장을 선택한 횟수
    selection_count INTEGER DEFAULT 0,

    -- 거리 가중치
    distance_weight DOUBLE PRECISION DEFAULT 0.5,

    -- 가격 가중치
    price_weight DOUBLE PRECISION DEFAULT 0.5,

    -- 가중치 업데이트 일시
    updated_at TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT preferences_id_fkey
        FOREIGN KEY (id)
        REFERENCES users(id)
        ON DELETE CASCADE
);