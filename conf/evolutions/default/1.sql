# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bank_account (
  id                            bigint not null,
  iban                          varchar(255),
  entity                        varchar(255),
  office                        varchar(255),
  control_digit                 varchar(255),
  account_number                varchar(255),
  balance                       decimal(18,2) not null,
  description                   varchar(255),
  user_id                       bigint,
  constraint pk_bank_account primary key (id)
);
create sequence bank_account_seq;

create table financial_movement (
  id                            bigint not null,
  expense                       boolean,
  concept                       varchar(255),
  transaction_date              timestamp,
  amount                        decimal(18,2) not null,
  financial_movement_type_id    bigint,
  financial_movement_subtype_id bigint,
  bank_account_id               bigint,
  user_id                       bigint,
  constraint pk_financial_movement primary key (id)
);
create sequence financial_movement_seq;

create table financial_movement_subtype (
  id                            bigint not null,
  description                   varchar(255),
  financial_movement_type_id    bigint,
  constraint pk_financial_movement_subtype primary key (id)
);
create sequence financial_movement_subtype_seq;

create table financial_movement_type (
  id                            bigint not null,
  description                   varchar(255),
  user_id                       bigint,
  constraint pk_financial_movement_type primary key (id)
);
create sequence financial_movement_type_seq;

create table consumer (
  id                            bigint not null,
  auth_token                    varchar(255),
  username                      varchar(255) not null,
  pass                          varchar(255),
  name                          varchar(255),
  constraint pk_consumer primary key (id)
);
create sequence consumer_seq;

alter table bank_account add constraint fk_bank_account_user_id foreign key (user_id) references consumer (id) on delete restrict on update restrict;
create index ix_bank_account_user_id on bank_account (user_id);

alter table financial_movement add constraint fk_financial_movement_financial_movement_type_id foreign key (financial_movement_type_id) references financial_movement_type (id) on delete restrict on update restrict;
create index ix_financial_movement_financial_movement_type_id on financial_movement (financial_movement_type_id);

alter table financial_movement add constraint fk_financial_movement_financial_movement_subtype_id foreign key (financial_movement_subtype_id) references financial_movement_subtype (id) on delete restrict on update restrict;
create index ix_financial_movement_financial_movement_subtype_id on financial_movement (financial_movement_subtype_id);

alter table financial_movement add constraint fk_financial_movement_bank_account_id foreign key (bank_account_id) references bank_account (id) on delete restrict on update restrict;
create index ix_financial_movement_bank_account_id on financial_movement (bank_account_id);

alter table financial_movement add constraint fk_financial_movement_user_id foreign key (user_id) references consumer (id) on delete restrict on update restrict;
create index ix_financial_movement_user_id on financial_movement (user_id);

alter table financial_movement_subtype add constraint fk_financial_movement_subtype_financial_movement_type_id foreign key (financial_movement_type_id) references financial_movement_type (id) on delete restrict on update restrict;
create index ix_financial_movement_subtype_financial_movement_type_id on financial_movement_subtype (financial_movement_type_id);

alter table financial_movement_type add constraint fk_financial_movement_type_user_id foreign key (user_id) references consumer (id) on delete restrict on update restrict;
create index ix_financial_movement_type_user_id on financial_movement_type (user_id);


# --- !Downs

alter table bank_account drop constraint if exists fk_bank_account_user_id;
drop index if exists ix_bank_account_user_id;

alter table financial_movement drop constraint if exists fk_financial_movement_financial_movement_type_id;
drop index if exists ix_financial_movement_financial_movement_type_id;

alter table financial_movement drop constraint if exists fk_financial_movement_financial_movement_subtype_id;
drop index if exists ix_financial_movement_financial_movement_subtype_id;

alter table financial_movement drop constraint if exists fk_financial_movement_bank_account_id;
drop index if exists ix_financial_movement_bank_account_id;

alter table financial_movement drop constraint if exists fk_financial_movement_user_id;
drop index if exists ix_financial_movement_user_id;

alter table financial_movement_subtype drop constraint if exists fk_financial_movement_subtype_financial_movement_type_id;
drop index if exists ix_financial_movement_subtype_financial_movement_type_id;

alter table financial_movement_type drop constraint if exists fk_financial_movement_type_user_id;
drop index if exists ix_financial_movement_type_user_id;

drop table if exists bank_account;
drop sequence if exists bank_account_seq;

drop table if exists financial_movement;
drop sequence if exists financial_movement_seq;

drop table if exists financial_movement_subtype;
drop sequence if exists financial_movement_subtype_seq;

drop table if exists financial_movement_type;
drop sequence if exists financial_movement_type_seq;

drop table if exists consumer;
drop sequence if exists consumer_seq;

