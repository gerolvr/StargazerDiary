# StargazerDiary

# Table of Content
* [Presentation](#presentation)
* [Interface](#interface)
  * [My Telescopes](#my-telescopes)
  * [My Observations](#my-observations)
  * [Astronomical Data](#astronomical-data)
  * [Registration, Authentication and Authorization](#registration-authentication-and-authorization)
* [Caching](#caching)
* [Custom error pages](#custom-error-pages)
  
# Presentation

[Link to the live website](http://gertest.hopto.org)

Stargazer Diary is a demo project.
The idea is to allow the user to keep a list of telescopes and observations.
It is mostly conceptual as the main purpose of the project is to use different technologies required in a full stack project, back-end and front-end.

* It is build with the Spring framework and other Spring projects: Boot, Data JPA, Security, Session, Web MVC, Devtools, Actuator.

* Data are stored in a MySql database but I also use a H2 in-memory database during development.

* For the view, I use Thymeleaf, HTML, CSS, Bootstrap, Jquery-ui.

* Javascript and CSS files are provided with Webjars.

* The same application also have a REST API for CRUD operation. Data are exchanged in JSON.
  See the [AngularJS website](http://gertest.hopto.org:81/) (and [repository](https://github.com/gerolvr/StargazerDiary-Frontend-AngularJS)) and the [Angular 4 website](http://gertest.hopto.org:82/) (and [repository](https://github.com/gerolvr/StargazerDiary-Frontend-Angular4)) front-ends.

* There are unit tests, slice tests and Selenium tests.

* CI: I have a Jenkins server which trigger a build everytime I commit to Github. The uber jar is then automatically uploaded to an Artifactory repository (running within a Docker container) so that every build is available.

* Git and Github for version control.

* Several Spring profiles, depending on the environment I need (dev, prod, etc.).

* The application is deployed on a AWS EC2 instance.

* Development is done on Linux with tools such as Spring Tool Suite (Eclipse), Visual Studio Code, Chrome, Curl, Postman, Maven.

# Interface
## My Telescopes
The user can manage a list of telescopes with basic operations: add, edit and delete.

![Telescopes](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/telescope1.png "Telescopes")
![Telescopes](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/telescope2.png "Telescopes")

## My Observations
The user can manage a list of observations: add, edit and delete.

![Observations](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/observation1.png "Observations")
![Observations](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/observation2.png "Observations")

## Astronomical Data
The user can search an astronomical object by name and get as results:
- The coordinates of the object.
- A planetarium view to help him visually locate the object in the sky (from [VirtualSky](https://github.com/slowe/VirtualSky), hosted locally on a Nginx web server).
- An optical view (from [Wikisky[(http://www.wikisky.org/)).
The planetarium and optical views are embedded iframes.

![AstroData](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/astrodatasearch.png "AstroData")

## Registration, Authentication and Authorization
For demo purpose the registration is simple, only a username and password is required to register and login. In a normal scenario, an email would be required to validate the registration and a Profile page would allow to update email and password.

![Register](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/register1.png "Register")

* If not logged in, the user is redirected to the login page:

![Login](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/login.png "Login")

* If the user name already exists he will get an error message:

![Register](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/register2.png "Register")

* A user cannot edit or delete another user telescope or observation. If he tries to meddle with the
url he will get a 401 error:

![401](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/401.png "401")

# Caching

Specifically in the Astronomical Data section, when a search is executed a rest service is consumed. [The search result is cached and another similar search will return immediately, improving the user experience.](https://github.com/gerolvr/StargazerDiary/blob/e8fafd4b3eec7a8ccdb354956a9d7a307e8b6919/src/main/java/com/gerolivo/stargazerdiary/services/AstroDataServiceImpl.java#L37 "The search result is cached and another similar search will return immediately, improving the user experience.")

# Custom error pages

A nice feature is to have a generic error page to handle many errors, i.e. a 4xx.html page in   [/src/main/resources/templates/error/4xx.html](http://https://github.com/gerolvr/StargazerDiary/tree/master/src/main/resources/templates/error "/src/main/resources/templates/error/4xx.html") will handle all 4xx errors (404,401,…)
![404](https://raw.githubusercontent.com/gerolvr/StargazerDiary/master/pictures/404.png "404")

