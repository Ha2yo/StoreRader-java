CREATE TABLE prices (
    -- 식별 번호
    id SERIAL PRIMARY KEY,

    -- 상품 번호
    good_id INTEGER NOT NULL,
    -- 매장 번호
    store_id INTEGER NOT NULL,
    -- 조사일자
    inspect_day VARCHAR(10) NOT NULL,
    -- 상품 가격
    price INTEGER NOT NULL,
    -- 1+1 여부
    is_one_plus_one VARCHAR(2),
    -- 할인 여부
    is_discount VARCHAR(2),
    -- 할인 시작일자
    discount_start VARCHAR(10),
    -- 할인 종료일자
    discount_end VARCHAR(10),

    -- 생성일자
    created_at TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT  prices_good_store_day_unique UNIQUE (good_id, store_id, inspect_day),

    CONSTRAINT prices_good_fk
        FOREIGN KEY (good_id)
        REFERENCES goods(good_id)
        ON DELETE CASCADE,

    CONSTRAINT prices_stores_fk
        FOREIGN KEY (store_id)
        REFERENCES stores(store_id)
        ON DELETE CASCADE
);