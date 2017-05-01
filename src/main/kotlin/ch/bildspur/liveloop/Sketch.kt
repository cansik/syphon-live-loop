package ch.bildspur.liveloop

import ch.bildspur.liveloop.controller.SyphonController
import ch.bildspur.liveloop.util.draw
import ch.bildspur.liveloop.util.format
import ch.bildspur.liveloop.util.imageRect
import controlP5.ControlP5
import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.opengl.PJOGL

/**
 * Created by cansik on 04.02.17.
 */
class Sketch : PApplet() {
    companion object {
        @JvmStatic val FRAME_RATE = 60f

        @JvmStatic val OUTPUT_WIDTH = 100
        @JvmStatic val OUTPUT_HEIGHT = 100

        @JvmStatic val WINDOW_WIDTH = 400
        @JvmStatic val WINDOW_HEIGHT = 400

        @JvmStatic val NAME = "Live Loop"

        @JvmStatic var instance = PApplet()

        @JvmStatic fun map(value: Double, start1: Double, stop1: Double, start2: Double, stop2: Double): Double {
            return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
        }
    }

    val syphon = SyphonController(this)

    var fpsOverTime = 0f

    lateinit var outputCanvas: PGraphics

    lateinit var cp5: ControlP5

    lateinit var buffer : LoopTextureBuffer

    init {

    }

    override fun settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, PConstants.P2D)
        PJOGL.profile = 1
    }

    override fun setup() {
        instance = this

        smooth()
        frameRate(FRAME_RATE)

        surface.setTitle(NAME)
        syphon.setupSyphonOutput(NAME)
        syphon.setupSyphonInput()

        cp5 = ControlP5(this)
        setupUI()

        outputCanvas = createGraphics(OUTPUT_WIDTH, OUTPUT_HEIGHT, PConstants.P2D)
    }

    var recording = true
    var playing = false

    override fun draw() {
        background(55f)

        if (frameCount < 2) {
            text("starting application...", width / 2 - 50f, height / 2f - 50f)
            return
        }

        val input = syphon.getGraphics()

        // check if input size changed
        if(input.width != outputCanvas.width || input.height != outputCanvas.height) {
            outputCanvas = createGraphics(input.width, input.height, P2D)
            buffer = LoopTextureBuffer(this, input.width, input.height, 5 * 60)
        }

        if(recording) {
            buffer.record(input)

            if(buffer.isFull) {
                recording = false
                println("recording off")
                playing = true
            }
        }

        // save frame
        outputCanvas.draw {

            if(playing)
                it.image(buffer.current(), 0f, 0f)
                else
                it.image(input, 0f, 0f)

        }

        syphon.sendImageToSyphon(outputCanvas)


        // draw output
        g.imageRect(outputCanvas, 0f, 0f, 400f, 280f)
        cp5.draw()
        drawFPS()
    }

    fun drawFPS() {
        // draw fps
        fpsOverTime += frameRate
        val averageFPS = fpsOverTime / frameCount.toFloat()

        textAlign(LEFT, BOTTOM)
        fill(255)
        text("FPS: ${frameRate.format(2)}\nFOT: ${averageFPS.format(2)}", 10f, height - 5f)
    }

    fun setupUI() {
        val h = 295f
        val w = 20f

        cp5.addButton("record")
                .setPosition(w, h)
                .setSize(120, 15)
                .onChange { e ->
                    playing = false
                    recording = true
                    buffer.index = 0
                }

        cp5.addButton("live")
                .setPosition(w + 100, h)
                .setSize(120, 15)
                .onChange { e ->
                    playing = false
                }
    }
}