CREATE TABLE invitation (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    organization_id UUID NOT NULL REFERENCES organization(id),
    email VARCHAR(150) NOT NULL,
    role VARCHAR(150) NOT NULL,
    token VARCHAR(150) NOT NULL UNIQUE,
    status VARCHAR(150) NOT NULL,
    invited_by_id UUID NOT NULL REFERENCES users(id),
    expires_at TIMESTAMP NOT NULL
)