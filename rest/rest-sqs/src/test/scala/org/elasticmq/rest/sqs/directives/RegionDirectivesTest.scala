package org.elasticmq.rest.sqs.directives
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}

class RegionDirectivesTest
    extends FlatSpec
    with Matchers
    with ScalatestRouteTest
    with Directives
    with RegionDirectives {

  "region directive" should "extract region header correctly" in {
    val route = path("test") {
      region { h =>
        _.complete(h)
      }
    }

    Post("/test") ~> RawHeader(
      "authorization",
      "AWS4-HMAC-SHA256 Credential=AKIAIKGQ552ZXMGBQGAA/20190219/eu-east-1/sqs/aws4_request") ~> route ~> check {
      responseAs[String] shouldEqual "eu-east-1"
    }

    Post("/test") ~> RawHeader(
      "authorization",
      "AWS4-HMAC-SHA256 Credential=AKIAIKGQ552ZXMGBQGAA/20190219/lol/sqs/aws4_request") ~> route ~> check {
      responseAs[String] shouldEqual ""
    }

    Post("/test") ~> RawHeader("authorization", "Basic") ~> route ~> check {
      responseAs[String] shouldEqual ""
    }

    Post("/test") ~> RawHeader("authorization", "AWS4-HMAC-SHA256 Credential=AKIAIKGQ552ZXMGBQGAA/20190219") ~> route ~> check {
      responseAs[String] shouldEqual ""
    }

    Post("/test") ~> route ~> check {
      responseAs[String] shouldEqual ""
    }

  }

}
