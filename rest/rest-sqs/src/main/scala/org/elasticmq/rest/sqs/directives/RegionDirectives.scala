package org.elasticmq.rest.sqs.directives

import akka.http.scaladsl.server.{Directive1, Directives}
import akka.stream.Materializer

// Sample header: Credential=AKIAIKGQ552ZXMGBQGAA/20190218/eu-east-1/sqs/aws4_request

trait RegionDirectives {
  this: Directives =>

  def region: Directive1[Option[String]] = {
    optionalHeaderValueByName("credential").tmap(value => value._1.map(_.split("/")).filter(_.length >= 3).map(_(2)))
  }

  implicit def materializer: Materializer

}
