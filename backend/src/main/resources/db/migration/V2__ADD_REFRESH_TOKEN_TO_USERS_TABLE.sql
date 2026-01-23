-- refresh_token
ALTER TABLE users ADD COLUMN refresh_token VARCHAR(255);
-- refresh_token 만료 시각
ALTER TABLE users ADD COLUMN refresh_token_expires_at TIMESTAMPTZ;

-- 원활한 조회를 위해 인덱스 생성
CREATE INDEX idx_users_refresh_token ON users (refresh_token);