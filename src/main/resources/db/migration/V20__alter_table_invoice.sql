ALTER TABLE invoice ADD COLUMN organization_id UUID REFERENCES organization(id);
UPDATE invoice SET organization_id = '99417234-890d-4718-8659-f065e9ef5f4f';
ALTER TABLE invoice ALTER COLUMN organization_id SET NOT NULL;

ALTER TABLE invoice ADD COLUMN created_by_id UUID REFERENCES users(id);
ALTER TABLE invoice ADD COLUMN issued_by_id UUID REFERENCES users(id);

ALTER TABLE invoice DROP CONSTRAINT invoice_number_key;
ALTER TABLE invoice ADD CONSTRAINT invoice_organization_id_number_key UNIQUE (organization_id, number);
