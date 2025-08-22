🏦 ATM Simulation
-------------------------------------------------------------------------------------------------------------------------------------------
A small project I built to practice Java, Spring Boot, and PostgreSQL by simulating how an ATM works.

It can run in two ways:
Console app → works directly from terminal (login, check balance, deposit, withdraw, reset pin, delete account).
REST APIs → exposed through Spring Boot controllers (so it’s ready to connect with a frontend later).

Tech Used
--------------------------------------------------------------------------------------------------------------------------------------------
-> Java 17

-> Spring Boot

-> PostgreSQL

-> Hibernate / JPA

Features
--------------------------------------------------------------------------------------------------------------------------------------------
Add new user (account number + 4-digit pin)
Secure login
Balance check, deposit, withdraw
Reset pin, delete account
Role based (admin/user)

How to Run
--------------------------------------------------------------------------------------------------------------------------------------------
Console:
cd src/main/java
javac com/atm/demo/AtmConsole.java
java com/atm/demo/AtmConsole

Spring Boot:
mvn spring-boot:run

Next Steps
---------------------------------------------------------------------------------------------------------------------------------------------
Thinking of adding transaction history, JWT authentication, and maybe a simple Angular UI.


