package ch.bildspur.liveloop.controller

import codeanticode.syphon.SyphonClient
import codeanticode.syphon.SyphonServer
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage

/**
 * Created by cansik on 04.02.17.
 */
class SyphonController(internal var sketch: PApplet) {

    lateinit var syphonOut: SyphonServer
    lateinit var syphonIn : SyphonClient

    fun setupSyphonOutput(name: String) {
        syphonOut = SyphonServer(sketch, name)
    }

    fun setupSyphonInput()
    {
        syphonIn = SyphonClient(sketch)
    }

    fun getGraphics() : PGraphics
    {
        return syphonIn.getGraphics(null)
    }

    fun sendImageToSyphon(p: PImage) {
        syphonOut.sendImage(p)
    }
}