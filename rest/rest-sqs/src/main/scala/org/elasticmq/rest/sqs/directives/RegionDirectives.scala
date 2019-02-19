package org.elasticmq.rest.sqs.directives

import akka.http.scaladsl.server.{Directive1, Directives}
import akka.stream.Materializer

// Sample header -> authorization: AWS4-HMAC-SHA256 Credential=AKIAIKGQ552ZXMGBQGAA/20190219/eu-east-1/sqs/aws4_request

trait RegionDirectives {
  this: Directives =>

  private val allowedRegions = Set(
    "us-east-1",
    "us-east-2",
    "us-west-1",
    "us-west-2",
    "ap-south-1",
    "ap-northeast-1",
    "ap-northeast-2",
    "ap-northeast-3",
    "ap-southeast-1",
    "ap-southeast-2",
    "ca-central-1",
    "cn-north-1",
    "cn-northwest-1",
    "eu-central-1",
    "eu-west-1",
    "eu-west-2",
    "eu-west-3",
    "eu-north-1",
    "eu-east-1",
  )

  def region: Directive1[Option[String]] = {
    optionalHeaderValueByName("authorization")
      .tmap(
        value =>
          value._1
            .filter(s => s.matches("AWS4-HMAC-SHA256 Credential=(.*)"))
            .map(_.split(" ")(1))
            .map(_.split("=")(1))
            .map(_.split("/"))
            .filter(_.length >= 3)
            .map(_(2))
            .filter(allowedRegions.contains)
      )
  }

  implicit def materializer: Materializer

}
