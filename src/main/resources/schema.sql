DROP TABLE IF EXISTS users;

CREATE TABLE "users" (
	"id" SERIAL NOT NULL,
	"username" varchar(64) NOT NULL PRIMARY KEY,
	"password" TEXT NOT NULL,
	"enabled" BOOLEAN NOT NULL
)
;

DROP TABLE IF EXISTS myusers;

CREATE TABLE "myusers" (
	"id" SERIAL NOT NULL,
	"username" varchar(64) NOT NULL PRIMARY KEY,
	"password" TEXT NOT NULL,
	"enabled" BOOLEAN NOT NULL
)
;

DROP TABLE IF EXISTS myauthorities;

CREATE TABLE "myauthorities" (
	"id" SERIAL NOT NULL,
	"username" varchar(64) NOT NULL PRIMARY KEY,
	"role" TEXT NOT NULL
)
;

DROP TABLE IF EXISTS authorities;

CREATE TABLE "authorities" (
	"id" SERIAL NOT NULL,
	"username" varchar(64) NOT NULL PRIMARY KEY,
	"authority" TEXT NOT NULL
)
;