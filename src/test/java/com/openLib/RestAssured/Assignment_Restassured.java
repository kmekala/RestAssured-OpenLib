package com.openLib.RestAssured;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import reporter.JyperionListener;

@Listeners(JyperionListener.class)
public class Assignment_Restassured {

    @DataProvider(name = "notes")
    public Object[][] createNotesData() {
        return new String[][]{{"type", "/type/text"},
                {"value", "Poem. In Sanskrit and Oriya (Oriya in Devanagari script); frwd. in English."}};
    }

    /*******************************************************
     * Send a GET request to http://openlibrary.org/authors/OL1A.json and check
     * that the answer has HTTP status code 200
     ******************************************************/

    @Test
    public void checkResponseCodeForCorrectRequest() {

        given()
                .when()
                .get("http://openlibrary.org/authors/OL1A.json")
                .then().assertThat()
                .statusCode(200);
    }

    /*******************************************************
     * Send a GET request to http://openlibrary.org/authors/incorrect.json and
     * check that the answer has HTTP status code 400
     ******************************************************/

    @Test
    public void checkResponseCodeForIncorrectRequest() {

        given()
                .when()
                .get("http://openlibrary.org/authors/incorrect.json")
                .then()
                .assertThat()
                .statusCode(404);
    }

    /*******************************************************
     * Send a GET request to http://openlibrary.org/authors/OL1A.json and check
     * that the response is in JSON format
     ******************************************************/

    @Test
    public void checkResponseContentTypeJson() {

        given().when()
                .get("http://openlibrary.org/authors/OL1A.json")
                .then()
                .assertThat()
                .contentType("application/json");
    }

    /***********************************************
     * Retrieve comment[1] information of the OL1M.json equals to reverted to
     * revision 46 Use
     * http://openlibrary.org/books/OL1M.json?m=history&limit=2&offset=1
     **********************************************/

    @Test
    public void checkTheSecondCommentOf2014WasAtAlbertPark() {

        given()
                .when()
                .get("http://openlibrary.org/books/OL1M.json?m=history&limit=2&offset=1")
                .then()
                .assertThat()
                .body("comment[1]", equalTo("reverted to revision 46"));
    }

    /***********************************************
     * Basic authentication use https://openlibrary.org/account/login
     **********************************************/

    @Test
    public void useBasicAuthentication() {
        given()
                .params("grant_type", "client_credentials")
                .auth()
                .preemptive()
                .basic("joe", "secret")
                .when()
                .post("https://openlibrary.org/account/login")
                .then()
                .log()
                .cookies();
    }

    /***********************************************
     * Retrieve key[0] information equals to /books/OL22562084M Use
     * http://openlibrary.org/query.json?type=/type/edition&authors=/authors/
     * OL1A&limit=2
     **********************************************/

    @Test
    public void useMultiplePathParameters() {

        given()
                .when()
                .get("http://openlibrary.org/query.json?type=/type/edition&authors=/authors/OL1A&limit=2")
                .then()
                .body("key[0]", equalTo("/books/OL22562084M"));
    }

    @Test
    public void useSinglePathParameter() {

        given()
                .pathParam("singleparam", "OL1A")
                .when()
                .get("http://openlibrary.org/authors/{singleparam}.json")
                .then()
                .assertThat()
                .contentType("application/json");
    }

    /***********************************************
     * Validate session information equals to /books/OL22562084M Use
     * http://openlibrary.org/query.json?type=/type/edition&authors=/authors/
     * OL1A&limit=2
     **********************************************/
    @Test
    public void cookiesSupportEqualCharacterInCookieValue() throws Exception {
        given()
                .params("grant_type", "client_credentials")
                .auth()
                .preemptive()
                .basic("joe", "secret")
                .cookie("jsessionid", "/user/joe%2C2009-02-19T07%3A52%3A13%2C74fc6%24811f4c2e5cf52ed0ef83b680ebed861f")
                /*.expect()
                .cookie("JSESSIONID", "/user/joe%2C2009-02-19T07%3A52%3A13%2C74fc6%24811f4c2e5cf52ed0ef83b680ebed861f")*/
                .when()
                .post("https://openlibrary.org/account/login");
    }

    /***********************************************
     * Retrieve the list of circuits for the 2014 season and check that it
     * contains silverstone
     **********************************************/

    @Test
    public void checkTheaurthor() {
        given()
                .when()
                .get("http://openlibrary.org/query.json?type=/type/edition&authors=/authors/OL1A&limit=2")
                .then()
                .assertThat()
                .body("key[1]", equalTo("/books/OL21399778M"));
    }

    /***********************************************
     * Retrieve the count of the response
     * http://openlibrary.org/works/OL27258W/editions.json?limit=5 5
     **********************************************/

    @Test
    public void checkSizeofentriesResponse() {
        given()
                .when()
                .get("http://openlibrary.org/works/OL27258W/editions.json?limit=5")
                .then()
                .body("entries.size()",equalTo(5));
    }

    /***********************************************
     * Retrieve the list of books for OL6807502M.rdf season and check that it
     * does contain publisher, title, issued, placeOfPublication
     **********************************************/

    @Test
    public void getRdfAndCheck() {

        given()
                .when()
                .get("https://openlibrary.org/books/OL6807502M.rdf")
                .then().assertThat().statusCode(200)
                .and()
                .body("RDF.Description.publisher", equalTo("Basic Books"))
                .and()
                .body("RDF.Description.title", equalTo("Code: and other laws of cyberspace"))
                .and()
                .body("RDF.Description.issued", equalTo("1999"))
                .and()
                .body("RDF.Description.placeOfPublication", equalTo("New York"));
    }

    /*******************************************************
     * Send a GET request to https://openlibrary.org/books/OL6807502M.rdf and
     * check that the response is in rdf+xml format
     ******************************************************/
    @Test
    public void checkResponseContectTypeRDF() {

        given().when().get("https://openlibrary.org/books/OL6807502M.rdf").then().assertThat()
                .contentType("application/rdf+xml");
    }

    // http://openlibrary.org/authors/OL1A.json

    @Test
    public void checkauthours() {

        given()
                .when()
                .get("http://openlibrary.org/authors/OL1A.json")
                .then().assertThat().statusCode(200)
                .and()
                .body("name", equalTo("Sachi Rautroy"))
                .and()
                .body("personal_name", equalTo("Sachi Rautroy"))
                .and()
                .body("last_modified.type", equalTo("/type/datetime"))
                .and()
                .body("death_date", equalTo("2004"))
                .and()
                .body("birth_date", equalTo("1916"));
    }

    @Test
    public void checkNumberofauthors() {

    }

    @Test
    public void retriveTitleandValidatefromResponse() {
        given().when().get("http://openlibrary.org/authors/OL1A/works.json").then()
                .body(containsString("Satchidananda Raut Roy"));

    }

    @Test
    public void validatenotes() {
        Response response = given().when()
                .get("http://openlibrary.org/query.json?type=/type/edition&authors=/authors/OL1A")
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();
        int sizeOfList = response.body().path("key.size()");

    }

    @Test
    public void whenExpectationsDefinedThenAsStringReturnsCanReturnTheResponseBody() throws Exception {
        final String body = expect()
                .body(equalTo(
                        "[{\"key\": \"/books/OL22562084M\"}, {\"key\": \"/books/OL21399778M\"}, {\"key\": \"/books/OL11921M\"}, {\"key\": \"/books/OL22867896M\"}, {\"key\": \"/books/OL4702164M\"}]"))
                .when().get("http://openlibrary.org/query.json?type=/type/edition&authors=/authors/OL1A&limit=5")
                .asString();

        /*assertThat(body, containsString("[{\"key\": \"/books/OL22562084M\"}, {\"key\": \"/books/OL21399778M\"}, {\"key\": \"/books/OL11921M\"}, {\"key\": \"/books/OL22867896M\"}, {\"key\": \"/books/OL4702164M\"}]"));*/
    }

    /*******************************************************
     * Send a GET request to
     * http://openlibrary.org/authors/OL1A.json?callback=process and check that
     * the response for callback
     ******************************************************/

    @Test
    public void callbackOpenLib() {
        given().when().get("http://openlibrary.org/authors/OL1A.json?callback=process").then().assertThat()
                .statusCode(200);
    }

    @Test
    public void checkResponseTime() {
        given().
                when().
                get("http://openlibrary.org/authors/OL1A.json?callback=process").
                then().
                assertThat().
                time(lessThan(500L),TimeUnit.MILLISECONDS);
    }

    @Test
    public void retrieveOAuthToken() {
        given().params("grant_type", "client_credentials").auth().preemptive().basic("joe", "secret").when()
                .post("https://openlibrary.org/account/login").then().log().body();
    }

    @AfterSuite
    public void tearDown() {
        sendPDFReportByGMail("kalyanfn@gmail.com", "DietC0ke", "kalyanfn@gmail.com","Test Report: Restful API Tests with RestAssured for OpenLib", "");
    }



    private static String randomEmail() {
        return "random-" + UUID.randomUUID().toString() + "@example.com";
    }

    /**
     * Send email using java
     *
     * @param from
     * @param pass
     * @param to
     * @param subject
     * @param body
     */
    private static void sendPDFReportByGMail(String from, String pass, String to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            // Set from address
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set subject
            message.setSubject(subject);
            message.setText(body);
            BodyPart objMessageBodyPart = new MimeBodyPart();
            objMessageBodyPart.setText("Please Find The Attached Report File!");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(objMessageBodyPart);
            objMessageBodyPart = new MimeBodyPart();
            // Set path to the pdf report file
            String filename = System.getProperty("user.dir") + "/Test.pdf";
            // Create data source to attach the file in mail
            DataSource source = new FileDataSource(filename);
            objMessageBodyPart.setDataHandler(new DataHandler(source));
            objMessageBodyPart.setFileName(filename);
            multipart.addBodyPart(objMessageBodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }

    }
}
