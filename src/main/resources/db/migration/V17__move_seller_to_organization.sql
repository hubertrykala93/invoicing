INSERT INTO organization (id, created_at, name, nip, street, postal_code, city, country, bank_account)
SELECT id, now(), name, nip, street, postal_code, city, country, bank_account
FROM seller;
