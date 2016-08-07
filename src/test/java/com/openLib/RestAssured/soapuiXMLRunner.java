package com.openLib.RestAssured;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.tools.SoapUITestCaseRunner;
import org.testng.Assert;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class soapuiXMLRunner {

    public void soapuiTestRunner(String filepath, String projectPassword, String testSuite) throws Exception {

        try {
            SoapUITestCaseRunner runner = new SoapUITestCaseRunner();
            runner.setJUnitReport(true);
            runner.setOutputFolder(System.getProperty("user.dir")+"\\target\\surefire-reports\\SoapUI_Reports\\"+System.getProperty("reportDirectory"));
            runner.setProjectFile(filepath);
            runner.setProjectPassword(projectPassword);
            runner.setSettingsFile(System.getProperty("user.dir")+"\\soapui-settings.xml");
            runner.setTestSuite(testSuite);
            runner.run();
            DirectoryStream<Path> directory = Files.newDirectoryStream(Paths.get(System.getProperty("user.dir")+"/target/surefire-reports/SoapUI_Reports/"+System.getProperty("reportDirectory")));
            for(Path file: directory){
                if(file.getFileName().toString().endsWith("FAILED.txt")){
                    Assert.fail("failed test!");
                    break;
                }
            }
            } catch (Exception e) {

            System.err.println("One or more Test cases failed");

        }
    }

    public void soapuiTestRunner(String filepath, String projectPassword) throws Exception {

        try {
            SoapUITestCaseRunner runner = new SoapUITestCaseRunner();
            runner.setJUnitReport(true);
            runner.setOutputFolder(System.getProperty("user.dir")+"\\target\\surefire-reports\\SoapUI_Reports\\"+System.getProperty("reportDirectory"));
            runner.setProjectFile(filepath);
            runner.setProjectPassword(projectPassword);
            runner.setSettingsFile(System.getProperty("user.dir")+"\\soapui-settings.xml");
            runner.run();
            DirectoryStream<Path> directory = Files.newDirectoryStream(Paths.get(System.getProperty("user.dir")+"/target/surefire-reports/SoapUI_Reports/"+System.getProperty("reportDirectory")));
            for(Path file: directory){
                if(file.getFileName().toString().endsWith("FAILED.txt")){
                    Assert.fail("failed test!");
                    break;
                }
            }

        } catch (Exception e) {

            System.err.println("One or more Test cases failed");

        }
    }



    public void runSoapUITest(String filePath, String projectPassword) throws Exception {

		/*
		Generic method to run a SoapUI Project.
		Required:
		1. Path to the *.xml project file
		2. projectPassword to open the file

		 */

        try{
            WsdlProject project = new WsdlProject(filePath,projectPassword);
            project.run( new PropertiesMap(), false );
            //project.setEncryptionStatus(WsdlProject.ProjectEncryptionStatus.NOT_ENCRYPTED);
            project.setShadowPassword(projectPassword);
            project.save();
            System.out.println("Project ran successfully");
        }
        catch (AssertionError e){
            System.out.println("Assertion failed");
        }

    }
}

