CREATE TABLE goods(
    -- 식별 번호
    id SERIAL PRIMARY KEY,

    -- 상품 번호
    good_id INTEGER,
    -- 상품 이름
    good_name VARCHAR(100) NOT NULL,
    -- 상품 용량
    total_cnt INTEGER,
    -- 상품 구분 코드
    total_div_code VARCHAR(5),

    -- 생성 일자
    created_at TIMESTAMPTZ DEFAULT NOW(),
    -- 수정 일자
    updated_at TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT goods_good_id_unique UNIQUE (good_id)
);