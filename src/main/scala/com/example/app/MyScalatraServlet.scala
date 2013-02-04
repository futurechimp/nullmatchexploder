package com.example.app

import org.scalatra._
import scalate.ScalateSupport

import atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s._

class MyServlet extends ScalatraServlet
with JacksonJsonSupport with AtmosphereSupport with SessionSupport with ScalateSupport {


  implicit protected val jsonFormats: Formats = DefaultFormats

  get("/:slug") {
    contentType="text/html"
    Data.all find (_.slug == params("slug")) match {
      case Some(page) => "success!" //ssp("/page", "page" -> page)
      case None => halt(404, "nope")
    }
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}

case class Foo(slug:String, body:String)


object Data {

  def all = List(Foo("slug1", "body1"), Foo("slug2", "body2"))

}