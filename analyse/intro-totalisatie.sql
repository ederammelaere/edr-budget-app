ALTER TABLE boekrekening ADD totalisatie BOOLEAN NOT NULL DEFAULT TRUE ;
ALTER TABLE boekrekening MODIFY totalisatie BOOLEAN NOT NULL ;