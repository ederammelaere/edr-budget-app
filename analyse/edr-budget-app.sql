drop table if exists boeking;
drop table if exists budget;
drop table if exists bankrekening;
drop table if exists boekrekening;
drop table if exists nextkey;

create table bankrekening
(
	id bigint not null auto_increment,
	version integer not null,
	rekeningnr varchar(50) not null,
	omschrijving varchar(100) not null,
	startsaldo decimal(10,2) not null,
	saldo decimal(10,2) not null,
	
	constraint pk_bankrekening primary key (id)
);

create table boekrekening
(
	id bigint not null auto_increment,
	version integer not null,
	rekeningnr varchar(15) not null,
	omschrijving varchar(100) not null,
	boekbaar boolean not null,
	budgeteerbaar boolean not null,
	
	constraint pk_boekrekening primary key (id)
);

create table boeking
(
	id bigint not null auto_increment,
	version integer not null,
	bankrekeningid bigint not null,
	boekrekeningid bigint not null,
	omschrijving varchar(100) not null,
	datum date not null,
	bedrag decimal(10,2),
	
	constraint pk_boeking primary key (id),
	constraint fk_boeking_bankrekening foreign key (bankrekeningid) references bankrekening(id),
	constraint fk_boeking_boekrekening foreign key (boekrekeningid) references boekrekening(id)
);

create table budget
(
	id bigint not null auto_increment,
	version integer not null,
	jaar integer not null,
	boekrekeningid bigint not null,
	bedrag decimal(10,2) not null,
	
	constraint pk_budget primary key (id),
	constraint fk_budget_boekrekening foreign key (boekrekeningid) references boekrekening(id)
);

create table nextkey
(
	entityname varchar(255) not null,
	sequence_next_hi_value bigint not null,

	constraint pk_nextkey primary key(entityname)
);
