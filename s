# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/T24DataRetrieval?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.initialize=true
spring.cloud.kubernetes.discovery.enabled=true

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect




