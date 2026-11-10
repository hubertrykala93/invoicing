CREATE TABLE contractor (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    nip VARCHAR(10) NOT NULL UNIQUE,
    street VARCHAR(100) NOT NULL,
    postal_code VARCHAR(6) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    email VARCHAR(155),
    defaultPaymentTermDays INTEGER NOT NULL DEFAULT 14,
    active BOOLEAN NOT NULL DEFAULT true
)