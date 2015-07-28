INSERT INTO customer_lang (partition_id, customer_id, language_level)
    VALUES (1, 1, 'English: C5');

INSERT INTO customer_lang (partition_id, customer_id, language_level)
VALUES (1, 3, 'English: C5');

SELECT customer.id, group_concat(language_level) FROM customer
JOIN customer_lang
ON customer.id = customer_lang.customer_id
GROUP BY customer.id;