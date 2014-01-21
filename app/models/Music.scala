package models

import java.nio.file.Path
import java.util.concurrent.CountDownLatch
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import scala.collection.JavaConverters._
import controllers.JavaScalaUtil._
import play.api.libs.json.Json

case class Music(val source: String, val artist: String, val album: String, val title: String)

object Music {

	object MusicMetadata extends Enumeration {
		type TrackMetadata = Value
		val artist, album, title = Value
	}

	// apply geht nicht wegen Json Macro
	def create(file: Path) = {
		val source = file.toString
		val metadata = getMetadata(file)
		val artist = metadata("artist").toString
		val album = metadata("album").toString
		val title = metadata("title").toString

		new Music(source, artist, album, title)
	}

	private def getMetadata(file: Path) = {
		val latch = new CountDownLatch(1)

		val uri = file.toUri.toString
		val media = new Media(uri)
		val player = new MediaPlayer(media)

		player.setOnReady(latch.countDown)
		latch.await

		media.getMetadata.asScala.toMap
	}

	implicit val musicReads = Json.reads[Music]
	implicit val musicWrites = Json.writes[Music]
}