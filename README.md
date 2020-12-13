# Keyist Ecommerce

Demo is now available at https://keyist.tech 🔑


![alt text](https://github.com/antkaynak/Keyist-Ecommerce/blob/master/screen_shots/detail.png)

![alt text](https://github.com/antkaynak/Keyist-Ecommerce/blob/master/screen_shots/cart.png)

![alt text](https://github.com/antkaynak/Keyist-Ecommerce/blob/master/screen_shots/browse.png)

![alt text](https://github.com/antkaynak/Keyist-Ecommerce/blob/master/screen_shots/orders.png)


## Getting Started
This project is a simple e-commerce website powered by Angular 10 on the frontend and Spring Boot for the backend.
<br>
For the full stack list please visit "Built With" section below.

Ecommerce is a complex business. It involves many edge cases and a solid system architecture. This project is simply a demo demonstrating basic features which is only the tip of an iceberg. This project might ( and certainly does ) have bugs, vulnerabilities or some other types of issues. So, contributions are always welcome :)

The demo is under free hosting, so it may require a bit of a patience :)

## Updated
This project is upgraded to Java 11, Spring Boot 2.3.3 and Angular 10+ and some new features added!

A docker-compose.yml added for you to bootstrap all of the aplication instances in one command!

## About This Project

* You can browse items or select an item from the showcase and preview.
* Browse section remembers your list choice, so even if you leave the page, when you come back you do not need to fetch products again.
* Your cart is saved on the database so you can login from different sources and still be able to use your cart.
* You can use discount coupons!
* You can store your information for faster purchase.
* You can view your orders and change your account settings.
* You can request a password forgot request and use the token in your mail.
* You can search items.


### Disclaimer
This is a fully functional demo site and may have security vulnerabilities as user data is not encrypted with SSL.
<br>
The provided codes are not ready for production and should only be used for education purposes.

### Prerequisites

What things you need to install

```
Angular CLI is recommended.
You need Tomcat server 8 or above installed or you can use embedded spring boot tomcat jar.
Locally installed MySQL or a MySQL server.
Compatible IDE, Intellij IDEA recommended for this project.

```

If you want to run the Dockerized version keep in mind that you must install Docker.
Also please note that booting up with docker-compose might take approx. 10-30min depending on your internet connection. After the initial setup, all other subsequent docker-compose commands will run much faster due to caching.

### Installing

The codes are split into 3 sections. 

frontend - Angular 10 ,

resource_server - Spring Boot Backend ,

authorization_server - A Spring Boot OAuth2 Authorization server.


To run the application fill the lines in resource server and authorization server application.properties as well as frontend application service urls.


For MySQL Database 

```
SQL script can be found in the sql folder.
script.sql contains both the basic user info and the tables for the oauth2 implementation.

```

For Tomcat Application Server

```
Use Tomcat 8.5.23 or above and compile to generate WAR file.
If you want you can use Spring Boot embedded tomcat JAR file and host it.
Keep in mind that both authorization and backend server's pom.xml is configured to compile a jar file.

```

For Undertow Application Server

```
This project uses undertow as default.
Keep in mind that both authorization and backend server's pom.xml is configured to compile a jar file.

```

This project uses Java Mail to create an embedded SMTP server. 
<br>
You can configure your own settings in the yaml file depending on your email provider or you can disable Java Mail altogether.
<br>

For Docker

```
In the root folder run the following command

docker-compose up

If you want to use development versions of the Dockerfiles, you need to setup docker-compose-yml to build using Dockerfile.dev.

```


## Built With

* [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) - Oracle Java 11 JDK
* [Angular](https://angular.io/) - Angular 10 Frontend
* [Rxjs](https://github.com/ReactiveX/rxjs) - Reactive functions
* [Ngrx Store](https://github.com/ngrx/store) - State management
* [Bootstrap](http://getbootstrap.com) - UI Components
* [NgBootstrap](https://ng-bootstrap.github.io/) - Angular specific widgets
* [FontAwesome](https://fontawesome.com/) - Icons
* [Spring](https://spring.io/) - Spring Boot resource and authorization server
* [Hibernate](http://hibernate.org/) - Object-relational Mapping
* [OAuth2](https://oauth.net/2/) - OAuth2 Authorization
* [Jackson](https://github.com/FasterXML/jackson) - JSON Object Mapping and data binding
* [Lombok](https://projectlombok.org/) - Automatic generated methods
* [MySQL](https://www.mysql.com/) - MySQL Database
* [Connector/J](https://dev.mysql.com/downloads/connector/j/5.1.html) - Connecting to MySQL Database Server
* [Maven](https://maven.apache.org/) - Dependency Management
* [Docker](https://www.docker.com/) - Containerized Apps
* [Docker Compose](https://docs.docker.com/compose/) -  Running multi-container Docker applications





## Known Bugs and Issues

* CORS filter allows requests from every url.
* You might be interested is not functional at this version.
* Sometimes when you refresh the page loading cart stucks.
* No admin panel at all as of yet.
* Shipping status only supports packaging and shipped ( 0-1 )
* No cargo firm selecting when purchasing.
* Mobile devices are not supported, yet. Contributons are welcome!

## Contributing

If you want to contribute to this project you can email me - antkaynak1@gmail.com or you can pull request.

## Versioning

This project does not have versioning and made with learning purposes.


## Authors 

* **Ant Kaynak** - *Initial work* - [Github](https://github.com/antkaynak)

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/antkaynak/Keyist-Ecommerce/blob/master/LICENSE) for details.

# Questions
If you have any questions mail me -  antkaynak1@gmail.com

