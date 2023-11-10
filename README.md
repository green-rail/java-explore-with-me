# java-explore-with-me

The final project in the Yandex.Praktikum java course. A RESTful web-service for posting and sharing events(lections, meetups etc) and find companions to participate in them. Users can post events, leave comments, register as participans, explore collections of events.

### Stack
Java, Spring Boot, PostgreSQL, JPA(Hibernate), Maven, Docker.

### Architecture
The app consists of two docker microservises: main API and statistics service. The main API is divided into three parts: public, for authentificated users and andministrator endpoints. 

### Build and run
 - Run mvn clean package 
 - Start the application with docker-compose up -d

https://github.com/green-rail/java-explore-with-me/pull/5
