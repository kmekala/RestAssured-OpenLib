# SkillSoftOpenLibAssignment

Preparation:

  ```
  Install Eclipse (or any other IDE)
  Install TestNG plugin (for Eclipse)
  Install m2e (or similar for any other IDE)
  Import Maven project into IDE, JDK 1.8
  Update project (Eclipse) or similar
  ```

   

Tools for testing RESTful web services:

  ```
  Browser (using plugins like Postman for Chrome)
  Open source (SoapUI, REST Assured)
  COTS (Parasoft SOAtest, SoapUI Pro)
  ```
 
Rest Assured:
 
  ```
  Java library for writing tests for RESTful web services
  Removes a lot of boilerplate code
  Integrates seamlessly with existing Java-based testing frameworks
    JUnit, TestNG
    Selenium WebDriver
  ```
    
Rest Assured Features:

  ```
  Support for HTTP methods (GET, POST, PUT, …)
  Support for BDD / Gherkin (Given/When/Then)
  Use of Hamcrest matchers for checks (equalTo)
  Use of GPath for selecting elements from JSON response
  ```
 
Hamcrest matchers:
 
   Express expectations in natural language

Examples:
 
   ```
      equalTo(X)      Is the object equal to x?
      hasItem(“X”)    Does the collection contain an item “X”
      hasSize(3)      Is the size of the collection equal to 3?
      not(equalto(X)) Inverts matcher equalTo()
  

http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html

   ```

Gpath:

  ```
  GPath is a path expression language integrated into Groovy 
  REST Assured is built in Groovy
  Similar aims and scope as XPath for XML
  ```

Documentation and examples:
   
   
    http://groovy-lang.org/processing-xml.html#_gpath
    http://groovy.jmiguel.eu/groovy.codehaus.org/GPath.html
    
    

Validating Technical response data:
   
    
    HTTP status code
    MIME-type of received responses
    Cookies and their value...  etc.

    

Our application under test:

  ```
   Open Library API
   Open Library provides a RESTful API for accessing resources in multiple formats.
   Data can be returned in JSON and RDF format
   API documentation at https://openlibrary.org/dev/docs/restful_api#save
   ```
  
Executing tests in CI:
    
    
    REST Assured-tests are no different from other Java (unit) tests
    Can be easily added to your CI/CD pipeline
    Part of the build process
   

To Execute the tests for RestAssured manually:
   
   ```
   right click on testng.xml and 
   Run as TestNG Suite,  testng.xml is located here 
   src/test/resources
   
   ```

To Execute existing SoapUI tests using TestNG runner
 ``` 
  
  Use: OpenLibREST.java (generic runner class to run SoapUI projects using TestNG runner)
  Maven cmd: mvn -f pom_soapui.xml -Dit.test=OpenLibREST.java -Dfilepath=”filepath” -DprojectPassword=”Password” -DtestSuite=”testSuite” verify
  
  ```


To execute the tests from terminal: 
   
   ```
  cd to the project folder
   mvn clean test
   ```

Report

```
Add your email in @AfterSuite in "Assignment_Restassured.java"
located here src/test/java/com/openLib/RestAssured/Assignment_Restassured.java, an pdf report will be emailed to you after the tests are executed.

```

Local reports

```
Standard HTML Report
A report will be generated at /target/surefire-reports/Suite/Test.html
```
