CREATE TABLE payment (
    id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL REFERENCES invoice(id),
    amount DECIMAL(12, 2) NOT NULL,
    payment_date DATE NOT NULL,
    method VARCHAR(150) NOT NULL,
    reference VARCHAR(100)
)