CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  "password" VARCHAR NOT NULL,
  preference VARCHAR NULL,
  weightUnit VARCHAR NULL,
  heightUnit VARCHAR NULL,
  weight INT NULL,
  height INT NULL,
  imageUri VARCHAR NULL,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_users_email_password ON users (email, "password");
