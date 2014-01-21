package controllers

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.Reads
import play.api.mvc.Results._

trait JsonRequest {

	def jsonRequest[T](f: T => Result)(implicit request: Request[JsValue], fjs: Reads[T]): Result =
		Json.fromJson[T](request.body) match {
			case JsSuccess(value, _) => f(value)
			case jsError @ JsError(_) => BadRequest(
				views.html.defaultpages.badRequest(request, formatJsonError(jsError))
			)
		}

	def formatJsonError(jsError: JsError): String =
		"Invalid Json-Format: \n" +
			("" /: jsError.errors)((msg, errorsInPath) => errorsInPath match {
				case (path, errors) => errors.map(error =>
					msg + "path: " + path.toJsonString + ", error: " + error.message + "\n"
				).mkString("\n")
			})
}