# Commit Viewer

This project consists of a Codacy challenge response.

## Getting Started

These instructions will get you the project up and running on your local machine for development and testing purposes.

## Running the tests

To run the Unit test just type the following maven command:

```
mvn clean test
```

## Running the API

To run the API Server you may also use a maven command for convenience:
 
 ```
mvn spring-boot:run
 ```

Note the fallback mechanism is using the github api as primary client. 
Please feel free to change that on the code (CommitViewerController constructor). 

You can test the API by using a simple HTTP Client, for example, Curl:

 ```
curl -X GET 'http://localhost:8080/api/repos/typetop/cats/commits?sha=master&page=1'
 ```

## Authors

* **Pedro Alipio** - *Initial work* - [Github](https://github.com/pmalipio)

