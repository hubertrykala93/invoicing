ALTER TABLE contractor DROP CONSTRAINT contractor_nip_key;
ALTER TABLE contractor ADD CONSTRAINT contractor_organization_id_nip_key UNIQUE (organization_id, nip);
