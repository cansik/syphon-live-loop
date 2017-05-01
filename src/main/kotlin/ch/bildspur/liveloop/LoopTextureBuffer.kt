package ch.bildspur.liveloop

import ch.bildspur.liveloop.util.draw
import processing.core.PApplet
import processing.core.PConstants.P2D
import processing.core.PGraphics

/**
 * Created by cansik on 01.05.17.
 */
class LoopTextureBuffer(val sketch : PApplet, val width : Int, val height : Int, val length : Int) {
    var index = 0

    private val buffer : Array<PGraphics>

    val isFull : Boolean
        get() = index >= length

    init {
        // init buffer
        buffer = (0..length).map { sketch.createGraphics(width, height, P2D) }.toTypedArray()
    }

    fun record(texture : PGraphics)
    {
        buffer[index++].draw {
            it.background(0f, 0f)
            it.image(texture, 0f, 0f)
        }
    }

    fun current() : PGraphics
    {
        if(index + 1 >= length)
            index = 0

        return buffer[index++]
    }
}