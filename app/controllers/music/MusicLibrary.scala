package controllers.music

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.FileVisitor
import java.nio.file.FileVisitResult
import java.nio.file.attribute.BasicFileAttributes
import java.io.IOException
import java.nio.file.Paths
import javafx.scene.media.Media
import javafx.collections.MapChangeListener
import javafx.beans.value.ChangeListener
import javafx.util.Duration
import javafx.beans.value.ObservableValue
import javafx.scene.media.MediaPlayer
import java.util.concurrent.CountDownLatch
import models.Music
import controllers.music.MusicLibrary._

object MusicLibrary {

	val supportedMusicTypes = Seq("MP3")

	def visitMusicFiles(dir: String, visitor: Path => Any) {
		val path = Paths.get(dir)
		visitFiles(path, file => {
			val fileName = file.getFileName.toString.toUpperCase
			if (supportedMusicTypes exists (fileName.endsWith(_))) {
				visitor(file)
			}
		})
	}

	private def visitFiles(dir: Path, visitor: Path => Any) =
		Files.walkFileTree(dir, new FileVisitor[Path]() {
			def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult =
				FileVisitResult.CONTINUE

			def visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
				if (!Files.isDirectory(path)) {
					visitor(path)
				}
				FileVisitResult.CONTINUE
			}

			def visitFileFailed(file: Path, e: IOException): FileVisitResult =
				FileVisitResult.CONTINUE

			def postVisitDirectory(file: Path, e: IOException): FileVisitResult =
				FileVisitResult.CONTINUE

		})
}

class MusicLibrary {

	var sources = List[String]()

	def addSource(source: String) {
		visitMusicFiles(source, file => Music.create(file))
	}
}

