CREATE TABLE organization (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    name VARCHAR(150) NOT NULL,
    nip VARCHAR(150) NOT NULL UNIQUE,
    street VARCHAR(100) NOT NULL,
    postal_code VARCHAR(6) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    bank_account VARCHAR(26) NOT NULL UNIQUE
)