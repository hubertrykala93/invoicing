CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    last_login_at TIMESTAMP,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(150) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    failed_login_attempts INTEGER,
    locked_until TIMESTAMP
)