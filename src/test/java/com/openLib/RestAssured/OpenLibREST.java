package com.openLib.RestAssured;


import org.testng.annotations.Test;

public class OpenLibREST {


      @Test
      public void runSoapTest() throws Exception {
          soapuiXMLRunner runner=new soapuiXMLRunner();
          String testSuite = System.getProperty("testSuite");
          String projectPassword = System.getProperty("projectPassword");
          String filePath = System.getProperty("filePath");
          runner.soapuiTestRunner(filePath,projectPassword,testSuite);

      }
    
  }
