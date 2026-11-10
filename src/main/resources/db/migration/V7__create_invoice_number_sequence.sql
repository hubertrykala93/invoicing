CREATE TABLE invoice_number_sequence (
    id UUID PRIMARY KEY,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    last_number INTEGER NOT NULL,
    UNIQUE (year, month)
)