package controllers

import play.api._
import play.api.mvc._
import controllers.music.MusicPlayer
import controllers.music.MusicLibrary
import play.api.libs.json.Json
import models.Music

object MusicPlayerApplication extends Controller with JsonRequest {

	val musicPlayer = MusicPlayer()
	val musicLibrary = MusicLibraryApplication.musicLibrary

	def getCurrentPlaylist = Action {
		val currentPlaylist = musicPlayer.currentPlaylist
		Ok(Json.toJson(currentPlaylist))
	}

	def addMusicToCurrentPlaylist = Action(parse.json) { implicit request =>
		jsonRequest[List[Music]] { musics =>
			musicPlayer.add(musics)
			Ok
		}
	}

	def playMusic = Action {
		musicPlayer.play
		Ok
	}

	def pauseMusic = Action {
		musicPlayer.pause
		Ok
	}

	def stopMusic = Action {
		musicPlayer.stop
		Ok
	}

	def playNextMusic = Action {
		musicPlayer.next
		Ok
	}

	def playPreviousMusic = Action {
		musicPlayer.previous
		Ok
	}
}