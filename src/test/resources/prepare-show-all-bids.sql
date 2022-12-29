insert into specialist(first_name, last_name, password, username, email, status, score)
values ('spe1fName', 'spe1lName', 'spe1password', 'spe1uName', 'spe1@mail.com', 'ACCEPTED', 5.0),
       ('spe2fName', 'spe2lName', 'spe2password', 'spe2uName', 'spe2@mail.com', 'ACCEPTED', 3.0),
       ('spe3fName', 'spe3lName', 'spe3password', 'spe3uName', 'spe3@mail.com', 'ACCEPTED', 2.0),
       ('spe4fName', 'spe4lName', 'spe4password', 'spe4uName', 'spe4@mail.com', 'ACCEPTED', 1.0),
       ('spe5fName', 'spe5lName', 'spe5password', 'spe5uName', 'spe5@mail.com', 'ACCEPTED', 0);
with custom_id as (insert into customer (first_name, last_name, password, username, created_at, email)
    values ('custom1fName', 'custom1lName', 'custom1pass', 'custom1lName', now()::timestamp, 'custom1@mail.com')
    returning id)
   , or_id as (insert into orders (address, customer_offer_price, description,
                                   status, working_time, customer_id, sub_service_id)
    select 'ord1add',
           2000.0,
           'desc',
           'WAITING_FOR_BID',
           now()::timestamp,
           custom_id.id,
           1
    from custom_id
    returning id)
insert
into bid (offer_price, start_working, end_working, order_id, specialist_id)
select 5002.4, now()::timestamp, now() ::timestamp, or_id.id,
(select id from specialist where username = 'spe1uName')
from or_id
union
select 5001.5, now()::timestamp, now() ::timestamp, or_id.id,
       (select id from specialist where username = 'spe2uName')
from or_id
union
select 5003.6, now()::timestamp, now()::timestamp, or_id.id,
       (select id from specialist where username = 'spe3uName')
from or_id
 union
 select 5004.6, now()::timestamp, now()::timestamp, or_id.id,
        (select id from specialist where username = 'spe4uName')
from or_id
 union
 select 5005.6, now()::timestamp, now()::timestamp, or_id.id,
        (select id from specialist where username = 'spe5uName')
from or_id;