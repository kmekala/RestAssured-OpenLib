package com.openLib.RestAssured;

import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class OpenLibStressTest {
    Component component;
    static final int wait = 60 * 1000;
    int iterations = 30;
    String post = "TEST";
    String expect = "HANG-TEST";
    String url = null;

    @Before
    public void setUp() throws Exception {
        url = "http://openlibrary.org/authors/OL1A.json";
        component = new Component();
        component.start();
        RestAssured.config = RestAssuredConfig.config().connectionConfig(new ConnectionConfig().closeIdleConnectionsAfterEachResponse());
    }

    @After
    public void tearDown() throws Exception {
        component.stop();
        RestAssured.reset();
    }

    @Test(timeout = wait)
    public void stressWithRestAssuredGet() throws UnsupportedEncodingException {
        for (int i = 0, n = iterations; i < n; i++) {
            given().
                    expect().body(equalTo(expect)).
                    when().get(url);
        }
    }

    @Test(timeout = wait)
    public void stressWithRestAssuredPost() throws UnsupportedEncodingException {
        for (int i = 0, n = iterations; i < n; i++) {
            given().contentType("text/plain;charset=utf-8").body(post.getBytes("UTF-8")).
                    expect().body(equalTo(expect)).
                    when().post(url);
        }
    }

    @Test(timeout = wait)
    public void stressWithRestAssuredPostWhenSameHttpClientInstanceIsReused() throws UnsupportedEncodingException {
        RestAssured.config = RestAssuredConfig.newConfig().httpClient(HttpClientConfig.httpClientConfig().reuseHttpClientInstance());

        try {
            for (int i = 0, n = iterations; i < n; i++) {
                given().contentType("text/plain;charset=utf-8").body(post.getBytes("UTF-8")).
                        expect().body(equalTo(expect)).
                        when().post(url);
            }
        } finally {
            RestAssured.reset();
        }
    }

    @Test(timeout = wait)
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


