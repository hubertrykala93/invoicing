ALTER TABLE invoice_number_sequence ADD COLUMN organization_id UUID REFERENCES organization(id);
UPDATE invoice_number_sequence SET organization_id = '99417234-890d-4718-8659-f065e9ef5f4f';
ALTER TABLE invoice_number_sequence ALTER COLUMN organization_id SET NOT NULL;

ALTER TABLE invoice_number_sequence DROP CONSTRAINT invoice_number_sequence_year_month_key;
ALTER TABLE invoice_number_sequence ADD CONSTRAINT invoice_number_sequence_organization_id_year_month_key UNIQUE (organization_id, year, month);
