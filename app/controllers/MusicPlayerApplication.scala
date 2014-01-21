package controllers

import play.api._
import play.api.mvc._
import controllers.music.MusicPlayer
import controllers.music.MusicLibrary
import play.api.libs.json.Json

object MusicPlayerApplication extends Controller {

	val musicPlayer = MusicPlayer()
	val musicLibrary = new MusicLibrary()

	musicLibrary.addSource("""E:\Eigene Dateien\Eigene Musik\Lindsey Stirling""")
	musicPlayer.add(musicLibrary.musics)

	def getCurrentPlaylist = Action {
		val currentPlaylist = musicPlayer.currentPlaylist
		Ok(Json.toJson(currentPlaylist))
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