Distributed Tracing Comparison
=====

The repository has 3 branches off of the master branch. One for each of 3 distributed tracing implementations.
The master branch contains 4 distinct [Spring Boot](https://spring.io/projects/spring-boot) services that all work together as a single distributed application. 
This application is a simple (unintelligent) ad targeting system for an imaginary travel agency. The system architecture looks 
like the following.

```mermaid
  UI Service --> User Preference Service;
  User Preference Service --> Mongo DB;
  Ad Service --> AI Ad Finder Service;
  AI Ad Finder Service --> User Preference Service;
```