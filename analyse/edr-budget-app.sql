drop table if exists boeking;
drop table if exists journaal;
drop table if exists budget;
drop table if exists bankrekening;
drop table if exists boekrekening;
drop table if exists nextkey;

create table bankrekening
(
	id bigint not null,
	version integer not null,
	rekeningnr varchar(50) not null,
	omschrijving varchar(100) not null,
	startsaldo decimal(10,2) not null,
	saldo decimal(10,2) not null,
	
	constraint pk_bankrekening primary key (id)
);

create unique index idx_bankrekening1 on bankrekening (rekeningnr);
create unique index idx_bankrekening2 on bankrekening (omschrijving);

create table boekrekening
(
	id bigint not null,
	version integer not null,
	rekeningnr varchar(15) not null,
	omschrijving varchar(100) not null,
	boekbaar boolean not null,
	budgeteerbaar boolean not null,
	
	constraint pk_boekrekening primary key (id)
);

create unique index idx_boekrekening1 on boekrekening (rekeningnr);
create unique index idx_boekrekening2 on boekrekening (omschrijving);

create table journaal
(
	id bigint not null,
	version integer not null,
	bankrekeningid bigint not null,
	datum date not null,
	afschriftnummer integer not null,
	transactienummer integer not null,
	tegenpartij_rekening varchar(255) not null,
	tegenpartij_rekeningid bigint,
	tegenpartij_naam varchar(255) not null,
	tegenpartij_adres varchar(255) not null,
	tegenpartij_plaats varchar(255) not null,
	transactie varchar(255) not null,
	valutadatum date not null,
	bedrag decimal(10,2) not null,
	devies varchar(10) not null,
	bic varchar(20) not null,
	landcode varchar(20) not null,
	
	constraint pk_journaal primary key(id),
	constraint fk_journaal_bankrekening1 foreign key(bankrekeningid) references bankrekening(id),
	constraint fk_journaal_bankrekening2 foreign key(tegenpartij_rekeningid) references bankrekening(id)
);

create unique index idx_journaal1 on journaal(datum, afschriftnummer);
create unique index idx_journaal2 on journaal(transactienummer, datum);

create table boeking
(
	id bigint not null,
	version integer not null,
	bankrekeningid bigint not null,
	boekrekeningid bigint not null,
	omschrijving varchar(100) not null,
	datum date not null,
	bedrag decimal(10,2),
	journaalid bigint,
	
	constraint pk_boeking primary key (id),
	constraint fk_boeking_bankrekening foreign key (bankrekeningid) references bankrekening(id),
	constraint fk_boeking_boekrekening foreign key (boekrekeningid) references boekrekening(id),
	constraint fk_boeking_journaal foreign key (journaalid) references journaal(id)
);

create index idx_boeking1 on boeking (datum);

create table budget
(
	id bigint not null,
	version integer not null,
	jaar integer not null,
	boekrekeningid bigint not null,
	bedrag decimal(10,2) not null,
	
	constraint pk_budget primary key (id),
	constraint fk_budget_boekrekening foreign key (boekrekeningid) references boekrekening(id)
);

create index idx_budget1 on budget (jaar);

create table nextkey
(
	entityname varchar(255) not null,
	sequence_next_hi_value bigint not null,

	constraint pk_nextkey primary key(entityname)
);
