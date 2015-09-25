package org.rga

import java.net.URL

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.specs2._
import org.specs2.mutable.BeforeAfter

class SimpleRestSpec extends mutable.Specification {

  sequential

  "Get..." >> new HttpServerMocked {
    stubFor(
      get(urlEqualTo("/"))
        .willReturn(aResponse()
        .withStatus(200)
        .withBody("{\"success\":true}")))

    val url: URL = new URL("https://localhost:3334")
    val response: Response = new SimpleRestClient(url).get("").request()

    response.asString must_== "{\"success\":true}"
    response.code must_== 200
  }

  "Post..." >> new HttpServerMocked {
    stubFor(
      post(urlEqualTo("/contactos"))
        .withHeader("Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
        .withRequestBody(WireMock.equalToJson("{\"value\": 3}"))
        .willReturn(aResponse()
        .withStatus(200)
        .withBody("{\"success\":true}")))


    val url: URL = new URL("http://localhost:3333")
    val response = new SimpleRestClient(url)
      .post("contactos")
      .header("Content-Type", "application/json; charset=UTF-8")
      .body("{\"value\":3}")
      .request()

    response.code must_== 200
  }

  "Put..." >> new HttpServerMocked {
    stubFor(
      put(urlEqualTo("/contactos/23"))
        .withHeader("Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
        .withRequestBody(WireMock.equalToJson("{\"value\": 3}"))
        .willReturn(aResponse()
        .withStatus(200)
        .withBody("{\"success\":true}")))


    val url: URL = new URL("http://localhost:3333")
    val response = new SimpleRestClient(url)
      .put("contactos", "23")
      .header("Content-Type", "application/json; charset=UTF-8")
      .body("{\"value\":3}")
      .request()

    response.code must_== 200
  }

  "Patch..." >> new HttpServerMocked {
    stubFor(
      patch(urlEqualTo("/contactos/23"))
        .withHeader("Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
        .withRequestBody(WireMock.equalToJson("{\"value\": 3}"))
        .willReturn(aResponse()
        .withStatus(200)
        .withBody("{\"success\":true}")))


    val url: URL = new URL("http://localhost:3333")
    val response = new SimpleRestClient(url)
      .patch("contactos", "23")
      .header("Content-Type", "application/json; charset=UTF-8")
      .body("{\"value\":3}")
      .request()

    response.code must_== 200
  }

  "With Auth..." >> new HttpServerMocked {
    stubFor(
      get(urlEqualTo("/"))
//        .withHeader("Authorization", WireMock.containing("Basic"))
        .willReturn(aResponse()
        .withStatus(200)
        .withBody("{\"success\":true}")))

    val url: URL = new URL("https://localhost:3334")
    val response: Response = new SimpleRestClient(url)
      .withAuth("user", "pass")
      .get("").request()

    response.asString must_== "{\"success\":true}"
    response.code must_== 200
  }

}

trait HttpServerMocked extends BeforeAfter {
  val wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(3333).httpsPort(3334))

  override def before = {
    wireMockServer.start()
    configureFor("localhost", 3333)
  }

  override def after = {
    wireMockServer.stop()
  }
}
