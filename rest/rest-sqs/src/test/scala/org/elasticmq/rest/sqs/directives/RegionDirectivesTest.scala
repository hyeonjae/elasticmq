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

    Post("/test") ~> RawHeader("credential", "AKIAIKGQ552ZXMGBQGAA/20190218/eu-east-1/sqs/aws4_request") ~> route ~> check {
      responseAs[String] shouldEqual "eu-east-1"
    }

    Post("/test") ~> RawHeader("credential", "AKIAIKGQ552ZXMGBQGAA/20190218") ~> route ~> check {
      responseAs[String] shouldEqual ""
    }

    Post("/test") ~> route ~> check {
      responseAs[String] shouldEqual ""
    }
    
  }

}
