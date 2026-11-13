CREATE TABLE membership (
    id UUID PRIMARY KEY,
    joined_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    organization_id UUID NOT NULL REFERENCES organization(id),
    role VARCHAR(150) NOT NULL
)