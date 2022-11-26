# Stranded Deep Companion App

## API

### Database

**TODO**: Persist DB / more permanent dev env

`docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=SOMEPASSWORD mysql/mysql-server:latest-aarch64`

`docker ps`

`docker exec -it [CONTAINER_ID] bash`

`mysql -p`

`CREATE DATABASE sdca;`

`CREATE USER 'shipstorm'@'%' IDENTIFIED BY 'password';`

`GRANT ALL PRIVILEGES ON sdca.* TO 'sdca'@'%' WITH GRANT OPTION;`

`FLUSH PRIVILEGES`

`exit`

### Useful SQL commands

`DROP TABLE user, user_sequence;`

### Useful curl requests

#### Create a new ENTITY
`curl -X POST http://localhost:8080/ENTITY_PLURAL -H "Content-Type: application/json" -d '{"some": "data"}'`

#### Get ENTITY by id
`curl http://localhost:8080/ENTITY_PLURAL/{id} -H "Content-Type: application/json"`

#### List ENTITIES
`curl http://localhost:8080/ENTITY_PLURAL -H "Content-Type: application/json"`