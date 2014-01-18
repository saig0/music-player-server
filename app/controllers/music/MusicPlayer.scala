package controllers.music

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javax.swing.SwingUtilities
import javafx.embed.swing.JFXPanel
import java.util.concurrent.CountDownLatch
import java.nio.file.Files
import java.nio.file.Paths
import controllers.JavaScalaUtil._
import scala.util.Random
import models.Music

object MusicPlayer {

	def apply() = {
		val player = new MusicPlayer
		player.init
		player
	}
}

class MusicPlayer {

	var shuffle = false
	var repeat = false

	var currentPlaylist = List[Music]()
	var currentMusic = 0
	var currentMediaPlayer: Option[MediaPlayer] = None

	/**
	 * Initialisiert Laufzeitumgebung für JavaFX, die von MediaPlayer benötigt wird.
	 */
	private def init {
		val latch = new CountDownLatch(1)
		SwingUtilities.invokeLater {
			new JFXPanel
			latch.countDown
		}
		latch.await
	}

	def play(pl: List[Music]) {
		currentPlaylist = List[Music]()
		currentMusic = 0
		add(pl)
		playNextTrack
	}

	def add(pl: List[Music]) {
		if (shuffle) {
			currentPlaylist ++= Random.shuffle(pl)
		} else {
			currentPlaylist ++= pl
		}
	}

	private def playNextTrack {
		val track = currentPlaylist(currentMusic)

		val uri = Paths.get(track.source).toUri toString
		val media = new Media(uri)
		val mediaPlayer = new MediaPlayer(media)
		currentMediaPlayer = Some(mediaPlayer)
		mediaPlayer.play

		if ((currentMusic + 1) < currentPlaylist.size) {
			mediaPlayer setOnEndOfMedia (next)
		} else if (repeat) {
			mediaPlayer setOnEndOfMedia (next)
		}
	}

	def pause {
		currentMediaPlayer map (_.pause)
	}

	def play {
		currentMediaPlayer map (_.play) getOrElse playNextTrack
	}

	def stop {
		currentMediaPlayer map (_.stop)
	}

	def next {
		stop
		currentMusic += 1
		if (currentMusic >= currentPlaylist.size) {
			currentMusic = 0
		}
		playNextTrack
	}

	def previous {
		stop
		currentMusic -= 2
		if (currentMusic < 0) {
			currentMusic = currentPlaylist.size - currentMusic
		}
		playNextTrack
	}
}