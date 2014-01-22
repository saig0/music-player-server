package models

import java.nio.file.Path
import java.util.concurrent.CountDownLatch
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import scala.collection.JavaConverters._
import controllers.JavaScalaUtil._
import play.api.libs.json.Json

case class Music(id: Long, val source: String, val artist: String, val album: String, val title: String)

object Music {

	import anorm._
	import anorm.SqlParser._
	import play.api.db._
	import play.api.Play.current

	val music = {
		get[Long]("id") ~
			get[String]("source") ~
			get[String]("artist") ~
			get[String]("album") ~
			get[String]("title") map {
				case id ~ source ~ artist ~ album ~ title =>
					Music(id, source, artist, album, title)
			}
	}

	def all: List[Music] = DB.withConnection { implicit c =>
		SQL("select * from music").as(music *)
	}

	def findById(id: Long): Music = DB.withConnection { implicit c =>
		SQL("select * from music where id = {id}").on(
			'id -> id
		).as(music.single)
	}

	def artists: List[String] = DB.withConnection { implicit c =>
		SQL("select distinct artist from music").as(scalar[String] *)
	}

	def albumsOfArtist(artist: String): List[String] = DB.withConnection { implicit c =>
		SQL("select distinct album from music where lower(artist) like {artist}").on(
			'artist -> ("%" + artist.toLowerCase + "%")
		).as(scalar[String] *)
	}

	// apply geht nicht wegen Json Macro
	def create(file: Path) = {
		val source = file.toString
		val metadata = getMetadata(file)
		val artist = metadata("artist").toString
		val album = metadata("album").toString
		val title = metadata("title").toString

		val id = DB.withConnection { implicit c =>
			SQL("insert into music (source, artist, album, title) values ({source}, {artist}, {album}, {title})").on(
				'source -> source,
				'artist -> artist,
				'album -> album,
				'title -> title
			).executeInsert() match {
					case Some(id) => id
					case None => 0
				}
		}
		new Music(id, source, artist, album, title)
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