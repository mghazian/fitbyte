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

CREATE INDEX ON users (email, "password");

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

CREATE TABLE IF NOT EXISTS activities (
	id BIGSERIAL PRIMARY KEY,
	activity_type_id BIGINT REFERENCES activity_types(id) ON UPDATE CASCADE ON DELETE SET NULL,
	doneAt TIMESTAMP NOT NULL,
	durationInMinutes INT NOT NULL,
	
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP NULL
);

CREATE INDEX ON activities (activity_type_id);