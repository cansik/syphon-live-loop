package ch.bildspur.liveloop.util

import processing.core.PGraphics
import processing.core.PImage
import java.lang.String


/**
 * Created by cansik on 04.02.17.
 */
fun Float.format(digits: Int) = String.format("%.${digits}f", this)

fun Float.isApproximate(value: Double, error: Double): Boolean {
    return (Math.abs(Math.abs(this) - Math.abs(value)) < error)
}

fun PGraphics.draw(block: (g: PGraphics) -> Unit) {
    this.beginDraw()
    block(this)
    this.endDraw()
}

fun PGraphics.cross(x: Float, y: Float, size: Float) {
    this.line(x, y - size, x, y + size)
    this.line(x - size, y, x + size, y)
}

fun PGraphics.imageRect(image : PImage, x : Float, y : Float, width : Float, height : Float)
{
    val ratio = if(width - image.width < height - image.height) width / image.width else height / image.height
    this.image(image, x, y, image.width * ratio, image.height * ratio)
}