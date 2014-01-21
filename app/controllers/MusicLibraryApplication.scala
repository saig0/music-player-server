package controllers

import play.api._
import play.api.mvc._
import controllers.music.MusicPlayer
import controllers.music.MusicLibrary
import play.api.libs.json.Json

object MusicLibraryApplication extends Controller with JsonRequest {

	val musicLibrary = new MusicLibrary()

	def addSource = Action(parse.json) { implicit request =>
		jsonRequest[String] { source =>
			musicLibrary.addSource(source)
			Ok
		}
	}

	def getAll = Action {
		Ok(Json.toJson(musicLibrary.musics))
	}
}