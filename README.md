# Commit Viewer

This project consists of a Codacy challenge response.

## Assumtions

Listing commits using the API takes the branch as an optional argument. 
If the branch is not passed it is assumed to be the master.

The commits list includes the following fields:
* commmitId
* author
* date
* message
Since these are the default for git log.

If a page is not found it returns an empty list.

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

Please feel free to change the fallback order it the commitviewer.properties.
You can also change command line execution configuration such as the command timeout and the
number of results per page. 

You can test the API by using a simple HTTP Client, for example, Curl:

 ```
curl -X GET 'http://localhost:8080/api/repos/typetop/cats/commits?sha=master&page=1'
 ```

## Authors

* **Pedro Alipio** - *Initial work* - [Github](https://github.com/pmalipio)

