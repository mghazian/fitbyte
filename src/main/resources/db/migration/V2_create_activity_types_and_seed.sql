CREATE TABLE IF NOT EXISTS activity_types (
  id BIGSERIAL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  calories_per_minute INT NOT NULL,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL
);

INSERT INTO activity_types ("name", calories_per_minute) VALUES
  ('Walking', 4),
  ('Yoga', 4),
  ('Stretching', 4),
  ('Cycling', 8),
  ('Swimming', 8),
  ('Dancing', 8),
  ('Hiking', 10),
  ('Running', 10),
  ('HIIT', 10),
  ('JumpRope', 10);