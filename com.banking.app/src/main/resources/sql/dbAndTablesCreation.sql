CREATE DATABASE bank;

CREATE TABLE banks
(
	id bigserial
		CONSTRAINT banks_pk
			PRIMARY KEY,
	name varchar,
	phone varchar,
	type varchar,
	able_to_buy_currency_online boolean,
	number_of_branches int,
	address varchar
);

CREATE TABLE currencies
(
	id bigserial
		CONSTRAINT currencies_pk
			PRIMARY KEY,
	bank_id bigserial NOT NULL
		CONSTRAINT currencies_banks_id_fk
			REFERENCES banks(id),
	name varchar,
	short_name varchar,
	purchase_rate numeric,
	selling_rate numeric
);
