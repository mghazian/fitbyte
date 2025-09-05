CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  "name" VARCHAR NULL,
  email VARCHAR NOT NULL,
  "password" VARCHAR NOT NULL,
  preference VARCHAR NULL,
  weight_unit VARCHAR NULL,
  height_unit VARCHAR NULL,
  weight INT NULL,
  height INT NULL,
  image_uri VARCHAR NULL,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_users_email_password ON users (email, "password");
