create database vrtech_intermediario;

use vrtech_intermediario;

create table cartoes (
	numero bigint not null,
	saldo decimal(19,2) not null,
  	senha varchar(8) not null,
  	
  	primary key (numero)
);

create table transacoes (
	id bigint not null auto_increment,
    data_hora datetime not null,
    valor decimal(19,2) not null,
    numero_cartao bigint not null,
   	
    primary key (id),
    foreign key (numero_cartao) references cartoes (numero)
);
