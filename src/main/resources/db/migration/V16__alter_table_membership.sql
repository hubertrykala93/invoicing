ALTER TABLE membership ADD CONSTRAINT membership_user_id_organization_id_key UNIQUE (user_id, organization_id);
