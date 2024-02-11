drop schema real_estate cascade;

create schema real_estate;

set search_path to real_estate;

create table user_details (
	id serial primary key,
	first_name varchar(100) not null,
	last_name varchar(100) not null,
	username varchar(50) not null unique,
	email varchar(50) not null unique,
	phone_number varchar(15) not null unique check (phone_number similar to '[0-9]{3} [0-9]{3} [0-9]{3}'),
	password varchar(100) not null,
	role varchar(10) not null,
	verified boolean not null default false
);

create table customer (
	id serial primary key,
	user_id integer not null references user_details(id) on delete cascade on update cascade
);

create table owner (
	id serial primary key,
	user_id integer not null references user_details(id) on delete cascade on update cascade
);

create table agent (
	id serial primary key,
	user_id integer not null references user_details(id) on delete cascade on update cascade
);

create table admin (
	id serial primary key,
	user_id integer not null references user_details(id) on delete cascade on update cascade
);

create table estate (
	id serial primary key,
	owner_id integer not null references owner(id) on update cascade on delete set default,
	agent_id integer not null references agent(id) on update cascade on delete set default,
	type varchar(20) not null,
	bathrooms integer not null check(bathrooms > 0),
	rooms integer not null check(rooms > 0),
	garage boolean not null,
	storey integer not null check(storey > 0),
	location varchar(100) not null,
	balcony boolean not null,
	description text not null,
	availability varchar(20) not null,
	size numeric(10, 2) not null check(size > 0),
	condition varchar(100) not null,
	offered_price numeric(10, 2) not null check(offered_price > 0),
	post_data timestamp not null default now()
);

create table offer (
	id serial primary key, 
	estate_id integer not null references estate(id) on delete cascade on update cascade,
	price numeric(10, 2) not null check(price > 0),
	description text not null,
	post_date timestamp not null default now()
);

create table photo (
	id serial primary key,
	estate_id integer not null references estate(id) on delete cascade on update cascade,
	filename varchar(100) not null,
	content_type varchar(100) not null,
	file_size integer not null,
	upload_data timestamp not null default now()
);

create table document (
	id serial primary key,
	estate_id integer not null references estate(id) on delete cascade on update cascade,
	filename varchar(100) not null,
	description text not null,
	upload_date timestamp not null default now()
);

create table offer_history (
	id serial primary key,
	estate_id integer not null references estate(id) on delete cascade on update cascade,
	price numeric(10, 2) not null,
	description text not null,
	archive_date timestamp not null default now()
);

create table customer_review (
	id serial primary key,
	customer_id integer not null references customer(id) on delete cascade on update cascade,
	offer_id integer not null references offer_history(id) on delete cascade on update cascade,
	rating integer not null check(rating > 0 and rating <= 10),
	comment text not null,
	post_date timestamp not null default now()
);

create table callendar (
	id serial primary key,
	agent_id integer not null references agent(id) on delete cascade on update cascade,
	date timestamp not null
);

create table likes (
	customer_id integer not null references customer(id) on delete cascade on update cascade,
	offer_id integer not null references offer(id) on delete cascade on update cascade
);

create table owner_meeting (
	id serial primary key,
	owner_id integer not null references owner(id) on delete cascade on update cascade,
	offer_id integer not null references offer(id) on delete cascade on update cascade,
	agent_id integer not null references agent(id) on delete cascade on update cascade,
	date timestamp not null
);

create table customer_meeting (
	id serial primary key,
	customer_id integer not null references customer(id) on delete cascade on update cascade,
	offer_id integer not null references offer(id) on delete cascade on update cascade,
	agent_id integer not null references agent(id) on delete cascade on update cascade,
	date timestamp not null
);

create table offer_visits (
	id serial primary key,
	customer_id integer not null references customer(id) on delete cascade on update cascade,
	offer_id integer not null references offer(id) on delete cascade on update cascade,
	date timestamp not null default now()
);

create table preference (
	id serial primary key,
	name varchar(100) not null unique
);

create table preference_customer (
	customer_id integer not null references customer(id) on delete cascade on update cascade,
	preference_id integer not null references preference(id) on delete cascade on update cascade
);

create table credit_worthiness (
	id integer primary key,
	customer_id integer not null references customer(id) on delete cascade on update cascade,
	age integer not null check(age >= 18),
	marital_status varchar(50) not null,
	employment_status varchar(50) not null,
	income numeric(8, 2) not null check(income > 0),
	debts numeric(8, 2) not null check(income >= 0),
	education_level varchar(50) not null,
	gender varchar(50) not null,
	expenses numeric(8, 2) not null check(expenses > 0)
);

