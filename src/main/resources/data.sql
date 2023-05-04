insert into chains(chain_name) VALUES('Chain 1');
insert into chains(chain_name) VALUES('Chain 2');

insert into products(product_code, product_desc, product_category_code, product_category_name) VALUES(70149001, 'Product description 0001', 191116, 'Category 01');
insert into products(product_code, product_desc, product_category_code, product_category_name) VALUES(70158202, 'Product description 0227', 191123, 'Category 02');
insert into products(product_code, product_desc, product_category_code, product_category_name) VALUES(70173300, 'Product description 0251', 191123, 'Category 02');

insert into customers(customer_code, customer_name, chain_id) VALUES(5520898, 'Chain 1 00038', 1);
insert into customers(customer_code, customer_name, chain_id) VALUES(5471692, 'Chain 1 00035', 1);
insert into customers(customer_code, customer_name, chain_id) VALUES(5471700, 'Chain 1 00024', 1);
insert into customers(customer_code, customer_name, chain_id) VALUES(5517876, 'Chain 1 00008', 1);

insert into prices(product_id, regular_price, chain_id) VALUES(1, 93.79, 1);
insert into prices(product_id, regular_price, chain_id) VALUES(2, 29.89, 1);
insert into prices(product_id, regular_price, chain_id) VALUES(3, 49.51, 1);

insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-02-25', 1, 1, 1, 24, 2250.96, 'REGULAR');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-03-25', 1, 1, 1, 12, 1125.48, 'REGULAR');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-04-15', 1, 1, 1, 12, 1125.48, 'REGULAR');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2020-12-30', 2, 2, 1, 1056, 25618.56, 'PROMO');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-01-16', 2, 2, 1, 4944, 89980.8, 'PROMO');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2020-12-28', 2, 3, 1, 3168, 76855.68, 'PROMO');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2020-12-28', 2, 3, 1, 3168, 76855.68, 'REGULAR');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2020-12-28', 2, 3, 1, 8208, 149385.6, 'PROMO');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-02-19', 3, 4, 1, 240, 7941.6, 'PROMO');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-03-01', 3, 4, 1, 720, 23824.8, 'PROMO');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-03-05', 3, 4, 1, 1116, 36928.44, 'REGULAR');
insert into shipments(shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign) VALUES('2021-03-15', 3, 4, 1, 480, 15883.2, 'PROMO');