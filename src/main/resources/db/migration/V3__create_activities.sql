CREATE TABLE IF NOT EXISTS activities (
  id BIGSERIAL PRIMARY KEY,
  activity_type_id BIGINT REFERENCES activity_types(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL,
  doneAt TIMESTAMP NOT NULL,
  durationInMinutes INT NOT NULL,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_activities_activity_type_id ON activities (activity_type_id);
