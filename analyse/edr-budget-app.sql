drop table if exists boeking;
drop table if exists budget;
drop table if exists bankrekening;
drop table if exists boekrekening;

create table bankrekening
(
	id integer not null auto_increment,
	version integer not null,
	rekeningnr varchar(50) not null,
	omschrijving varchar(100) not null,
	startsaldo decimal(10,2) not null,
	saldo decimal(10,2) not null,
	
	constraint pk_bankrekening primary key (id)
);

create table boekrekening
(
	id integer not null auto_increment,
	version integer not null,
	rekeningnr varchar(15) not null,
	omschrijving varchar(100) not null,
	boekbaar boolean not null,
	budgeteerbaar boolean not null,
	
	constraint pk_boekrekening primary key (id)
);

create table boeking
(
	id integer not null auto_increment,
	version integer not null,
	bankrekeningid integer not null,
	boekrekeningid integer not null,
	omschrijving varchar(100) not null,
	datum date not null,
	bedrag decimal(10,2),
	
	constraint pk_boeking primary key (id),
	constraint fk_boeking_bankrekening foreign key (bankrekeningid) references bankrekening(id),
	constraint fk_boeking_boekrekening foreign key (boekrekeningid) references boekrekening(id)
);

create table budget
(
	id integer not null auto_increment,
	version integer not null,
	jaar integer not null,
	boekrekeningid integer not null,
	bedrag decimal(10,2) not null,
	
	constraint pk_budget primary key (id),
	constraint fk_budget_boekrekening foreign key (boekrekeningid) references boekrekening(id)
);
