INSERT INTO seller (id, name, nip, street, postal_code, city, country, bank_account)
VALUES (gen_random_uuid(), 'Hubert Dev Software', '7010012356',
        'ul. Programistów 1', '05-400', 'Otwock', 'Poland',
        '12345678901234567890123456');

INSERT INTO contractor (id, name, nip, street, postal_code, city, country, email, default_payment_term_days, active)
VALUES
    (gen_random_uuid(), 'Alfa Software sp. z o.o.', '1234563218', 'ul. Długa 5',      '00-950', 'Warszawa', 'Poland', NULL, 14, true),
    (gen_random_uuid(), 'Beta Systems S.A.',        '9876543210', 'ul. Morska 10',    '80-001', 'Gdańsk',   'Poland', NULL, 30, true),
    (gen_random_uuid(), 'Gamma Media sp. z o.o.',   '5554443334', 'ul. Floriańska 3', '31-019', 'Kraków',   'Poland', NULL,  7, true);
