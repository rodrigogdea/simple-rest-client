package org.rga;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleRestClientTest {

    private WireMockServer wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig().port(3333).httpsPort(3334));



    @Test
    public void get() throws MalformedURLException {
        stubFor(
                WireMock.get(urlEqualTo("/"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody("{\"success\":true}")));

        URL url = new URL("https://localhost:3334");
        Response response = new SimpleRestClient(url, true).get("").request();

        assertTrue(response.code() == 200);
        assertEquals("{\"success\":true}", response.asString());
    }

    @Test
    public void post() throws MalformedURLException {
        stubFor(
                WireMock.post(urlEqualTo("/contactos"))
                        .withHeader("Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
                        .withRequestBody(WireMock.equalToJson("{\"value\": 3}"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withBody("{\"success\":true}")));


        URL url = new URL("http://localhost:3333");
        Response response = new SimpleRestClient(url, false)
                .post("contactos")
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("{\"value\":3}")
                .request();

        String result = response.asString();

        assertEquals(201, response.code());
    }

    @Test
    public void put() throws MalformedURLException {
        stubFor(
                WireMock.put(urlEqualTo("/contactos/23"))
                        .withHeader("Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
                        .withRequestBody(WireMock.equalToJson("{\"value\": 3}"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withBody("{\"success\":true}")));


        URL url = new URL("http://localhost:3333");
        Response response = new SimpleRestClient(url, false)
                .put("contactos", "23")
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("{\"value\":3}")
                .request();

        String result = response.asString();

        assertEquals(201, response.code());
    }

    @Before
    public void before(){
        wireMockServer.start();
        configureFor("localhost", 3333);
    }

    @After
    public void after(){
        wireMockServer.stop();
    }

}
