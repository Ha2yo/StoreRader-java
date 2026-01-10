CREATE TABLE users (
    --- 식별 번호
    id SERIAL PRIMARY KEY,

    --- 구글 제공 sub
    sub VARCHAR(255) NOT NULL,
    --- 구글 제공 이메일
    email VARCHAR(255) NOT NULL,
    --- 구글 제공 사용자 이름
    name VARCHAR(100),

    --- 계정 생성일시
    created_at TIMESTAMPTZ DEFAULT NOW(),
    --- 마지막 로그인일시
    last_login TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT users_sub_unique UNIQUE (sub)
);