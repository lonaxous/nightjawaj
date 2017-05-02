create sequence seq_user start with 1;
create sequence seq_event start with 1;
create sequence seq_activity start with 1;
create sequence seq_organiser start with 1;
create sequence seq_ambiance start with 1;
create sequence seq_planing start with 1;

create table utilisateur(
	id int primary key,
	fname varchar2(30) not null,
	lname varchar2(30) not null,
	adrs varchar2(200),
	mail varchar2(100) not null,
	transport varchar2(20),
	foodpref varchar2(300)
);

create table event(
	id int primary key,
	name varchar2(50) not null,
	jour Date not null,
	starthour varchar2(10),
	endhour varchar2(10)
);

create table actvity(
	id int primary key,
	name varchar2(50) not null,
	adrs varchar2(200) not null,
	jour Date,
	starthour varchar2(10),
	endhour varchar2(10)
);

create table organiser(
	id int primary key,
	idu int references utilisateur,
	ide int references event
);

create table ambiance(
	id int primary key,
	idu int references utilisateur,
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
