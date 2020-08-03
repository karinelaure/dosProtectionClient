Client
-------------------------------------------------------------------
##  Requirements:
For building and running the application you need:
JDK 8 and Maven 4

## Installing and running:

Please note that Server must be running in same machine, otherwise you should change the server Ip address inside of project folder "/resources/application.properties".

Steps:
1. Clone this project to your server. 
2. Open cmd, navigate to the project folder.
3. Run: mvn clean install
4. Wait till Maven completes the build.
5. Navigate to target and run : java -jar dosProtectionClient.jar

--------------------------------------------
Used libraries in maven: 
httpcomponents - to send client request with managed connections
---------------------------------------------------
## Configuration
- configuration file (application.properties) used to configure request maximum and minimum time that threads sleeps, url and parameter query
## How it works

Buy starting app class dosProtectionExecutor starts to execute main method.

The user inputs the number of HTTP clients to simulate. 
1- For each request thread created, thread class is DosClient it  receive client number, url, param query name to create request. 
2- DosClient will creates random  clientId between 1 to given number of HTTP clients and send request.
3- When it gets response it will wait random time between 500 to 3000 milliseconds.
(it is configurable in application.properties) and starts step 3 again.
