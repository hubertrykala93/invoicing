CREATE TABLE refresh_token (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    token_hash VARCHAR(150) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL,
    replaced_by_id UUID REFERENCES refresh_token(id)
)