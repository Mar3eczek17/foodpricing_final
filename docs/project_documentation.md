# Project Documentation for `foodpricing`, Linux environment

# Table of Contents
  - [Database Setup Instructions](#database-setup-instructions)
    - [1. Accessing the Database Setup Script](#1-accessing-the-database-setup-script)
    - [2. Running the Setup Script](#2-running-the-setup-script)
    - [3. Encountered Issues](#3-encountered-issues)
    - [4. Troubleshooting Steps](#4-troubleshooting-steps)
    - [5. Manual Database Setup](#5-manual-database-setup)
    - [6. Project Status](#6-project-status)
  - [Installation and Deployment Guide for `foodpricing`](#installation-and-deployment-guide-for-foodpricing)
    - [Mandatory Steps](#mandatory-steps)
      - [1. Build the WAR File](#1-build-the-war-file)
      - [2. Start the Application from Command Line](#2-start-the-application-from-command-line)
      - [3. Deploying the Application (Deploying the WAR File to Tomcat)](#3-deploying-the-application-deploying-the-war-file-to-tomcat)
      - [4. Removing the Application from Tomcat](#4-removing-the-application-from-tomcat)
      - [5. For Recompiling after Java Version Update](#5-for-recompiling-after-java-version-update)
    - [Preferred Steps](#preferred-steps)
      - [1. Generate Javadoc](#1-generate-javadoc)
      - [2. Demonstrating Project Documentation](#2-demonstrating-project-documentation)
    - [Checkstyle Integration](#checkstyle-integration)
      - [1. Running Checkstyle Check](#1-running-checkstyle-check)
      - [2. Generate Checkstyle Report As Standalone](#2-generate-checkstyle-report-as-standalone)
      - [3. Checking for Violations as Part of the Build](#3-checking-for-violations-as-part-of-the-build)
  - [Process Termination on Port](#process-termination-on-port)





## Database Setup Instructions

### 1. Accessing the Database Setup Script:
- The database setup script is located at
`/media/marek/DATA/IdeaProjects/Final/foodpricing_final/src/main/resources/static/setup_database.sh`.

### 2. Running the Setup Script:
- Open a terminal and navigate to the directory containing the script.
- Use the following command to execute the script:
`sh /media/marek/DATA/IdeaProjects/Final/foodpricing_final/src/main/resources/static/setup_database.sh`.

### 3. Encountered Issues:
- The script may encounter errors such as:
  - `ddl_script.sql: No such file or directory`
  - `dml_script.sql: No such file or directory`
  - `ERROR 1045 (28000): Access denied for user 'marek'@'localhost' (using password: NO)`

### 4. Troubleshooting Steps:
- For file-related errors, ensure that the necessary SQL files (`ddl_script.sql` and `dml_script.sql`) are present in the specified locations.
- For the `Access denied` error, consider the following:
  - Verify the user credentials (`marek`) and ensure they have the necessary permissions to create and populate the database.
  - Check if the database user has the appropriate privileges (e.g., `CREATE`, `INSERT`) for the specified database.
  - Confirm if the database server is running and accessible.

### 5. Manual Database Setup:
- If the script encounters persistent issues, consider manually executing SQL commands using the MySQL client:
  - `mysql -u marek -p < ddl_script.sql`
  - `mysql -u marek -p < dml_script.sql`
- Enter the password when prompted.

### 6. Project Status:
- Once the database installation and data population steps are successful, the message `"Database installation and data population complete"` should be displayed.





## Installation and Deployment Guide for `foodpricing`

## Mandatory Steps:
### 1. Build the WAR File:
- Navigate to your project directory.
- Run the Maven command to clean and install the project, generating a WAR file in the target directory.
  - `mvn clean install`
  - This command compiles the code, runs tests, and packages the application.

### 2. Start the Application from Command Line:
- Navigate to the target directory.
- Run the application using java -jar.
  - `cd target`
  - `java -jar foodpricing.war`

### 3. Deploying the Application (Deploying the WAR File to Tomcat):
- Stop Tomcat (if running) using `sudo systemctl stop tomcat` within Terminal in main directory.

Deploying to Tomcat:
  - Copy the generated WAR file to Tomcat's webapps directory.
    - `sudo cp /media/marek/DATA/IdeaProjects/Final/foodpricing_final/target/foodpricing.war /opt/tomcat/webapps/`
    - Start Tomcat using `sudo systemctl start tomcat` within Terminal in main directory.
    - Access the deployed application at http://localhost:8080/foodpricing.

### 4. Removing the Application from Tomcat:
 - Stop Tomcat using `sudo systemctl stop tomcat` within Terminal in main directory.
 - Remove the WAR file and its extracted directory.
   - `sudo rm /opt/tomcat/webapps/foodpricing.war` in /opt/tomcat/webapps directory.
 - Start Tomcat again using `sudo systemctl start tomcat` within Terminal in main directory.

### 5. For Recompiling after Java Version Update:
- Recompile the Spring Boot application with the compatible Java version.
  - `./mvnw clean package` in Maven.
- Restart Tomcat to apply changes.
  - `sudo systemctl restart tomcat` within Terminal in main directory.





## Preferred Steps:
### 1. Generate Javadoc:
- Use the Maven command to generate Javadoc.
  - `mvn javadoc:javadoc`
- Javadoc will be generated in the **target/site/apidocs** directory.

### 2. Demonstrating Project Documentation:
- Accessing Javadoc:
  - Open the generated Javadoc in a web browser.
    - `xdg-open site/apidocs/index.html` in target directory.





## Checkstyle Integration:
### 1. Running Checkstyle Check:
- Execute the following Maven command to run the Checkstyle check during the validate phase:
  - `mvn validate`
- If there are any Checkstyle violations, they will be reported to the console, and the build will fail if the failsOnError configuration is set to true.

### 2. Generate Checkstyle Report As Standalone:
- You can also generate the Checkstyle report by explicitly executing the checkstyle:checkstyle goal from the command line. You are not required to specify the Checkstyle Plugin in your pom.xml unless you want to use a specific configuration.
  - `mvn checkstyle:checkstyle`

### 3. Checking for Violations as Part of the Build:
- To check for violations as part of the build, execute the following Maven command:
    - `mvn checkstyle:check`
- This command will analyze the code for Checkstyle violations during the build process.





## Process Termination on Port
- To kill the process running for example on port 8081, follow these steps:
  - Identify the process ID (PID) of the process running on port 8081:
    - Execute the following command in the terminal to identify the process running on port 8081:
      - `sudo lsof -i :8081`
  - Terminate the process using the PID:
    - `sudo kill <PID>`, replace <PID> with the actual PID you obtained from the previous command.
      - For example, if the PID of the process running on port 8081 is 1234, you would execute:
        - `sudo kill 1234`, after killing the process, you should be able to start your application on port 8081 without any issues.
  - After killing the process, you should be able to start your application on port 8081 without any issues.