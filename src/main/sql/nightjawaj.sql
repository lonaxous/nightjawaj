drop table utilisateur;
drop table event;
drop table activity;
drop table organiser;
drop table ambiance;
drop table planning;
drop table APIgoogle;


create table utilisateur(
	id integer primary key autoincrement,
	fname varchar(30) not null,
	lname varchar(30) not null,
	adrs varchar(200),
	mail varchar(100) not null,
	psw varchar(100),
	transport varchar(20),
	foodpref varchar(300),
	unique(mail)
);

create table event(
	id integer primary key autoincrement,
	name varchar(50) not null,
	startdate varchar(16),
	enddate varchar(16)
);

create table activity(
	id integer primary key autoincrement,
	name varchar(50) not null,
	adrs varchar(200) not null,
	startdate varchar(16),
	enddate varchar(16)
);

create table organiser(
	id integer primary key autoincrement,
	idu integer references utilisateur,
	ide integer references event
);

create table ambiance(
	id integer primary key autoincrement,
	idu integer references utilisateur,
	ide integer references event
);

create table planning(
	id integer primary key autoincrement,
	ide integer references event,
	ida integer references actvity
);

create table APIgoogle(
    id integer primary key,
    clef varchar(300)
);
