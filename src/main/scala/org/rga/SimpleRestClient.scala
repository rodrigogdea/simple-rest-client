package org.rga

import java.net.{URI, URL}
import java.nio.charset.Charset
import java.util
import java.util.Collections
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods._
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.message.{BasicNameValuePair, BasicHeader}
import org.apache.http.util.EntityUtils

import scala.collection.JavaConversions._


class SimpleRestClient(baseUrl: URL, ignoreSelfCertificate: Boolean = true) extends RestClient(baseUrl) {

  private val httpClient = {
    val httpClientBuilder: HttpClientBuilder = HttpClientBuilder.create
    if (ignoreSelfCertificate) httpClientBuilder.setSSLSocketFactory(TrustingSSLSocketFactory.get)
    httpClientBuilder.setDefaultHeaders(Collections.singletonList(new BasicHeader("Accept", "application/json")))
      .build
  }

  override def get(uriFragment: String*): GetBuilder = new GetBuilder(baseUrl, httpClient, uriFragment, new HttpGet())

  override def post(uriFragment: String*): WithBodyBuilder = new WithBodyBuilder(baseUrl, httpClient, uriFragment, new HttpPost())

  override def put(uriFragment: String*): WithBodyBuilder = new WithBodyBuilder(baseUrl, httpClient, uriFragment, new HttpPut())

  override def patch(uriFragment: String*): WithBodyBuilder = new WithBodyBuilder(baseUrl, httpClient, uriFragment, new HttpPatch())

}

class WithBodyBuilder(baseUrl: URL,
                      httpClient: CloseableHttpClient,
                      fragments: Seq[String],
                      httpEntityEnclosingRequestBase: HttpEntityEnclosingRequestBase)
  extends RequestBuilder[WithBodyBuilder](baseUrl, httpClient, fragments, httpEntityEnclosingRequestBase) {

  def body(content: String) = {
    httpEntityEnclosingRequestBase.setEntity(new StringEntity(content, Charset.defaultCharset))
    this
  }

  def form(values: Array[BasicNameValuePair]) = {
    httpEntityEnclosingRequestBase.setEntity(new UrlEncodedFormEntity(values.toList, "UTF-8"))
    this
  }
}

class GetBuilder(baseUrl: URL, httpClient: CloseableHttpClient, fragments: Seq[String], httpGet: HttpGet)
  extends RequestBuilder[GetBuilder](baseUrl, httpClient, fragments, httpGet) {

}

abstract class RequestBuilder[R <: RequestBuilder[R]](baseUrl: URL,
                                                      httpClient: CloseableHttpClient,
                                                      fragments: Seq[String],
                                                      verb: HttpRequestBase) {

  private lazy val uri: URI = new URL(baseUrl, fragments.mkString("/")).toURI

  def request(): Response = {
    verb.setURI(uri)
    new Response(httpClient.execute(verb))
  }

  def header(name: String, value: String): R = {
    verb.setHeader(name, value)
    this.asInstanceOf[R]
  }
}

class Response(response: CloseableHttpResponse) {

  def code = response.getStatusLine.getStatusCode

  def asString = EntityUtils.toString(response.getEntity)

  def asByteArray = EntityUtils.toByteArray(response.getEntity)

  def headers = response.getAllHeaders
}

