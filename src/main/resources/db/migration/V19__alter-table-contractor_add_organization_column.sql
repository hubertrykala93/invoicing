ALTER TABLE contractor ADD COLUMN organization_id UUID REFERENCES organization(id);
UPDATE contractor SET organization_id = '99417234-890d-4718-8659-f065e9ef5f4f';
ALTER TABLE contractor ALTER COLUMN organization_id SET NOT NULL;
