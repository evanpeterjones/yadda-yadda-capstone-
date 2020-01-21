# Maven – How to create a Java project
Maven 3, Apache Commons Codec, a simple Java project to hash a string with the SHA-256 algorithm.

Project Link - https://www.mkyong.com/maven/how-to-create-a-java-project-with-maven/

## How to run this project?
```
$ git clone https://github.com/mkyong/maven-examples.git
$ cd java-project
$ mvn package 
$ java -jar target/java-project-1.0-SNAPSHOT.jar 123456
```
Output
```
8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
```

## Compile as jar for other projects to use as dependency 
Once compiled to jar using 
``` 
$ mvn clean package
```
then...
```
$ mvn install:install-file -Dfile=target/java-project-1.0-SNAPSHOT.jar -DgeneratePom=true -Dcreatechecksum=true -Dpackaging=jar -DgroupId=com.mkyong.hashing -DartifactId=java-project -Dversion=1.0 
```
^ this command basically takes your jar file and moves it to your `.m2` directory which is where all java dependencies are stored! neat!