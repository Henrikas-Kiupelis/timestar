INSERT INTO customer_lang (partition_id, customer_id, language_level)
    VALUES (1, 1, 'English: C5');

INSERT INTO customer_lang (partition_id, customer_id, language_level)
VALUES (1, 3, 'English: C5');

SELECT customer.id, group_concat(language_level)
FROM customer
JOIN customer_lang
ON customer.id = customer_lang.customer_id
GROUP BY customer.id;

SELECT customer.id, group_concat(language_level)
FROM customer
JOIN customer_lang ON customer.id = customer_lang.customer_id
JOIN student ON customer.id = student.customer_id
JOIN student_group ON student_group.id = student.group_id
GROUP BY customer.id
ORDER BY customer.id
LIMIT 2;

SELECT count(*) FROM customer WHERE customer.partition_id = 1;