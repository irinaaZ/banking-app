INSERT INTO bank.public.banks (name, phone, type, able_to_buy_currency_online, number_of_branches, address)
VALUES ('Monobank', '0800205205', 'Global', true, 1, 'Kiev, 9 Institutska Street');

UPDATE bank.public.banks
SET type = 'Local'
WHERE id = 1;

SELECT *
FROM bank.public.banks
WHERE address LIKE '%Kiev%';

DELETE
FROM bank.public.banks
WHERE name = 'Monobank';

INSERT INTO bank.public.currencies (bank_id, name, short_name, purchase_rate, selling_rate)
VALUES (1, 'US Dollar', 'USD', 28.99, 29.55);

UPDATE bank.public.currencies
SET selling_rate = 29.76
WHERE name = 'US Dollar';

SELECT short_name
FROM bank.public.currencies
WHERE bank_id = 1;

DELETE
FROM bank.public.currencies
WHERE short_name = 'USD';

SELECT *
FROM bank.public.banks b
INNER JOIN bank.public.currencies c on b.id = c.bank_id;