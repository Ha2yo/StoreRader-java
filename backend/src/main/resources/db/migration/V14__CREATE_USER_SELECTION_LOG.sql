CREATE TABLE user_selection_log (
    -- 식별 번호
    id SERIAL PRIMARY KEY,

    -- users 테이블의 식별번호
    user_id INTEGER NOT NULL,

    -- 매장 id
    store_id INTEGER NOT NULL,

    -- 상품 id
    good_id INTEGER NOT NULL,

    -- 상품의 가격
    price INTEGER NOT NULL,

    -- 유저의 선호 타입
    preference_type VARCHAR(10) NOT NULL,

    -- 레코드 생성일시
    created_at TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT user_selection_log_user_id_fkey
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);