# My Java App

This is a simple Java application that uses Maven for dependency management and build automation.

## Project Structure

The project follows the standard Maven directory structure:

- `src/main/java`: This directory contains the application source code.
- `src/main/resources`: This directory contains resources required by the application such as configuration files.
- `src/test/java`: This directory contains the test source code.
- `src/test/resources`: This directory contains resources required by the tests.
- `pom.xml`: This is the Maven Project Object Model (POM) file. It contains information about the project and configuration details used by Maven to build the project.

## Building the Project

To build the project, navigate to the project root directory (the directory containing the `pom.xml` file) in a terminal and run the following command:

```bash
mvn clean install
```

This command cleans the project, compiles the source code, runs the tests and packages the application into a JAR file.

## Running the Application

To run the application, use the following command:

```bash
java -cp target/my-java-app-1.0-SNAPSHOT.jar com.Butterfly.App
```

Replace `my-java-app-1.0-SNAPSHOT.jar` with the name of the JAR file generated by the build process and `com.Butterfly.App` with the fully qualified name of your main class.

## Contributing

If you would like to contribute to this project, please feel free to fork the repository, make your changes and submit a pull request.