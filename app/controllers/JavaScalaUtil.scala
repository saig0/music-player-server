package controllers

object JavaScalaUtil {
	implicit def func2Runnable(f: => Any): Runnable =
		new Runnable() {
			def run = f
		}
}