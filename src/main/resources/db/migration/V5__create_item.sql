CREATE TABLE item (
    id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL REFERENCES invoice(id),
    position INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    quantity DECIMAL(12, 3) NOT NULL,
    unit VARCHAR(150) NOT NULL,
    net_unit_price DECIMAL(12, 2) NOT NULL,
    vat_rate VARCHAR(150) NOT NULL,
    net_value DECIMAL(12, 2) NOT NULL,
    vat_value DECIMAL(12, 2) NOT NULL,
    gross_value DECIMAL(12, 2) NOT NULL
)