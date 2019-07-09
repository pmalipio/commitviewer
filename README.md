# Commit Viewer

This project consists of a Codacy challenge response.

## Dependencies

This software depends on bash, git and sed.

## Assumtions

Listing commits using the API takes the branch as an optional argument. 
If the branch is not passed it is assumed to be the master.

The command line based commits list includes the following fields:
* commmitId - the commmit Id
* author - the author and email 
* date - date in the git log format
* message - the commit message.

Since these are the defaults for git log.

If a page is not found it returns an empty list.

## Implementation Notes

The command line parser is based on single lines which allowed a very convenient, elegant and fast solution for processing
the log into a data structure. 

Unfortunately, messages cannot be outputted by git log in a single line.

To overcome this issue, I used sed to replace linebreaks by the __NL__ tag. Each line has a
__EOL__ tag to explicitly mark the end of the line eventually replaced by a line break.

This is done by the following git commad:

```
git log --pretty=format:'commit:%H,author:%aN <%aE>,date:%ad,message:%B__EOL__'|sed ':a;N;$!ba;s/\n/__NL__/g'|sed 's/__EOL__/\n/g'
```

Then, in the java side, when processing the log lines the __NL__ tags are replaced by line breaks
again.


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

