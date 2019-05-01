# JCRM
[JCRM](https://crm.codehunters.ru) is an open source (GPL v3) Java based CRM.

## Dependencies for building
### java 11+
#### Ubuntu
```bash
sudo apt install openjdk-11-jdk
```
#### Windows
```bash
// TODO
```
#### MacOS
```bash
// TODO
```
### Docker
#### Ubuntu
https://docs.docker.com/install/linux/docker-ce/ubuntu/
#### Windows
https://docs.docker.com/docker-for-windows/install/
#### MacOS
https://docs.docker.com/docker-for-mac/install/

## Build
```bash
./gradlew buildSingleArtifact 
```


## Dependencies for running
### PostgreSQL
#### Ubuntu
```bash
sudo apt install postgresql postgresql-contrib
```
#### Windows
```bash
// TODO
```
#### MacOS
```bash
// TODO
```
By default, DB user should be 'postgres' with 'root' password.

## Run
```bash
./gradlew bootRun \
     -Djavax.mail.username=... \
     -Djavax.mail.password=... \
     -Djavax.mail.smtp.from=... \
     -Ddb.upgrading.enabled=true
```
