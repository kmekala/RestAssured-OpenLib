package com.openLib.RestAssured;

import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.commons.io.IOUtils;
import org.restlet.Component;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.assertEquals;

public class OpenLibStressTest {
    Component component;
    static final int wait = 60 * 1000;
    int iterations = 30;
    String post = "TEST";
    String expect = "{\"name\": \"Sachi Rautroy\", \"created\": {\"type\": \"/type/datetime\", \"value\": \"2008-04-01T03:28:50.625462\"}, \"death_date\": \"2004\", \"alternate_names\": [\"Satchidananda Raut Roy\", \"Satchidananda Raut-Roy\", \"Satchidananda Rautroy\", \"Satchi Raut Roy\", \"Satchi Raut-Roy\", \"Satchi Rautroy\", \"Saccida\\u0304nanda Ra\\u0304utara\\u0304y\\u0307a\", \"Yugashrashta Sachi Routray\", \"Sacci R\\u0101utar\\u0101\\u1e8fa\"], \"last_modified\": {\"type\": \"/type/datetime\", \"value\": \"2016-11-09T22:25:45.881010\"}, \"latest_revision\": 8, \"key\": \"/authors/OL1A\", \"birth_date\": \"1916\", \"personal_name\": \"Sachi Rautroy\", \"type\": {\"key\": \"/type/author\"}, \"revision\": 8}";
    String url = null;

    @BeforeMethod
	public void setUp() throws Exception {
        url = "http://openlibrary.org/authors/OL1A.json";
        component = new Component();
        component.start();
        RestAssured.config = RestAssuredConfig.config().connectionConfig(new ConnectionConfig().closeIdleConnectionsAfterEachResponse());
    }

    @AfterMethod
	public void tearDown() throws Exception {
        component.stop();
        RestAssured.reset();
    }

    @Test(timeOut = wait)
    public void stressWithRestAssuredGet() throws UnsupportedEncodingException {
        for (int i = 0, n = iterations; i < n; i++) {
            given().
                    expect().body(equalTo(expect)).
                    when().get(url);
        }
    }

    @Test(timeOut = wait)
    public void stressWithRestAssuredGetManualClose() throws IOException, InterruptedException {
        RestAssured.config = RestAssuredConfig.newConfig().httpClient(HttpClientConfig.httpClientConfig().reuseHttpClientInstance());

        try {
            for (int i = 0, n = iterations; i < n; i++) {
                String body = IOUtils.toString(get(url).andReturn().body().asInputStream());
                assertEquals(expect, body);
            }
        } finally {
            RestAssured.reset();
        }
    }
}


