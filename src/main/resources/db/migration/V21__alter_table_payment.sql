ALTER TABLE payment ADD COLUMN recorded_by_id UUID REFERENCES users(id);
