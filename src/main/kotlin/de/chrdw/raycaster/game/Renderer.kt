package de.chrdw.raycaster.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import java.nio.ByteBuffer


class Renderer {
    private val raycaster = RayCaster()

    private val map: Pixmap
    private val buffer: ByteBuffer

    init {
        map = Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888)
        buffer = map.pixels
    }

    fun texture(): Texture {
        return Texture(map)
    }

    fun draw(state: State) {
        clear()
        drawWalls(state)
    }

    private fun drawWalls(state: State) {
        val pos = state.player.position
        val angle = state.player.angle
        val dir = Vec2(Math.cos(angle), Math.sin(angle))
        val plane = Vec2(Math.cos(angle + Math.PI / 2), Math.sin(angle + Math.PI / 2)) * 0.7
        for (x in 0..WIDTH - 1) {
            val cameraX = 2.0 * x / WIDTH - 1
            val ray = dir + plane * cameraX
            val (dist, side, part, tile) = raycaster.ray(pos, ray, state.level)
            val height = Math.min((HEIGHT / dist).toInt(), HEIGHT )
            val color = mulColor(mulColor(tile.color, 3.0 / (side + 3)), part)
            val wallstart = HEIGHT / 2 - height/2
            for (h in 0..height - 1) {
                val y = wallstart + h
                buffer.putInt((x + y * WIDTH) * 4, color)
            }
        }
        buffer.flip()
    }

    fun mulColor(color: Int, mul: Double): Int {
        val r = ((color ushr 24) * mul).toInt()
        val g = ((color ushr 16 and 0xff) * mul).toInt()
        val b = ((color ushr 8 and 0xff) * mul).toInt()
        val a = color and 0xff
        return  (r shl 24) or (g shl 16) or (b shl 8) or a
    }

    private fun clear() {
        buffer.rewind()
        for (y in 0..HEIGHT - 1) {
            for (x in 0..WIDTH - 1) {
                if (y > HEIGHT / 2) {
                    buffer.putInt(FLOOR_COLOR)
                } else {
                    buffer.putInt(CEILING_COLOR)
                }
            }
        }
    }

}