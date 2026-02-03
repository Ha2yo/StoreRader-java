CREATE TABLE regions(
    -- 지역 코드
    code VARCHAR(10) NOT NULL,
    -- 지역 이름
    name VARCHAR(30) NOT NULL,
    -- 지역 상세 코드
    parent_code VARCHAR(10),
    -- 지역 레벨
    level INTEGER
);