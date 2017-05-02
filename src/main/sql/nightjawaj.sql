create sequence seq_user start with 1;
create sequence seq_event start with 1;
create sequence seq_activity start with 1;
create sequence seq_organiser start with 1;
create sequence seq_ambiance start with 1;
create sequence seq_planing start with 1;

create table user(
	id int primary key,
	fname varchar2 not null,
	lname varchar2 not null,
	adrs varchar2,
	mail varchar2 not null,
	transport varchar2,
	foodpref varchar2
);

create table event(
	id int primary key,
	name varchar2 not null,
	date Date not null,
	starthour varchar2,
	endhour varchar2
);

create table actvity(
	id int primary key,
	name varchar2 not null,
	adrs varchar2 not null,
	date Date,
	starthour varchar2,
	endhour varchar2
);

create table organiser(
	id int primary key,
	idu int references user,
	ide int references event
);

create table ambiance(
	id int primary key,
	idu int references user,
	ide int references event
);

create table planing(
	id int primary key,
	ide int references event,
	ida int references actvity 
);

create table APIgoogle(
    id int primary key,
    clef varchar2(300)
);
