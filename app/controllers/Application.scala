package controllers

import play.api._
import play.api.mvc._
import controllers.music.MusicPlayer
import controllers.music.MusicLibrary

object Application extends Controller {

	val musicPlayer = MusicPlayer()
	val musicLibrary = new MusicLibrary()

	musicLibrary.addSource("""E:\Eigene Dateien\Eigene Musik\Lindsey Stirling""")
	musicPlayer.add(musicLibrary.musics)

	def index = Action {
		Ok(views.html.index("Your new application is ready."))
	}
}