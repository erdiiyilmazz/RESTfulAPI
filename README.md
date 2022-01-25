# RESTfulAPI 

This codebase can be used as a prototype to manage users and profiles in a particular domain. 

RESTful API offers:

- Register, retrieve, update, add and remove user account data
- Register, retrieve, update, add and remove profile data
- Id based transactions available
- Create/add/remove permissions associated with user and profile data


Dockerfile is set to execute MySQL as a container and to expose REST api via Swagger UI. The other container is Java Microservice. 


db.cmd is first to be modified to set MySQL docker volume. Run db.cmd to start MySQL container.

./db.cmd


run.cmd bash script is to be executed to run the Java microservice container.

./run.cmd

It is supposed to explore REST api through browser:

http://localhost:8090/swagger-ui.html

Project would be containerized and available via browser.

Next phase will be these features and platforms along with React frontend.
