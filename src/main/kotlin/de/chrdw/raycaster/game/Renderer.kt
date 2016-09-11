package de.chrdw.raycaster.game

import com.badlogic.gdx.graphics.Pixmap
import java.nio.ByteBuffer


class Renderer {
    private val raycaster = RayCaster()

    private val map: Pixmap
    private val buffer: ByteBuffer
    private val texures = arrayOf(
            Texture("img/redbrick.png"),
            Texture("img/eagle.png"),
            Texture("img/mossy.png"),
            Texture("img/greystone.png"))

    init {
        map = Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888)
        buffer = map.pixels
    }

    fun texture(): com.badlogic.gdx.graphics.Texture {
        return com.badlogic.gdx.graphics.Texture(map)
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
            val (dist, side, wallX, tile) = raycaster.ray(pos, ray, state.level)
            val height = if (dist > 0) (HEIGHT / dist).toInt() else HEIGHT
            val texture = texures[tile.texture]
            val wallstart = HEIGHT / 2 - height / 2
            val y0 = Math.max(0, wallstart)
            val y1 = Math.min(HEIGHT-1, wallstart + height)
            for (y in y0..y1) {
                val h = (y-wallstart).toDouble() / height
                var color = texture.getPixel(wallX, h )
                color = mulColor(color, 2.0 / (2 + side))
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
        return (r shl 24) or (g shl 16) or (b shl 8) or a
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