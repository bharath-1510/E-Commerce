ALTER TABLE shipping_option ADD CONSTRAINT UK_01 UNIQUE (code);
ALTER TABLE shipping_option DROP COLUMN is_return;

