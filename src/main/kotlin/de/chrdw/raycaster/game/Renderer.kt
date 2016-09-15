package de.chrdw.raycaster.game

import com.badlogic.gdx.graphics.Pixmap
import java.nio.ByteBuffer
import java.util.*

enum class Textures(val id: Int) {
    Invalid(-1), RedBrick(0), Eagle(1), Mossy(2), GreyStone(3), Barrel(4), Pillar(5)
}


class Renderer {
    private val raycaster = RayCaster()

    private val map: Pixmap
    private val buffer: ByteBuffer
    private val depthBuffer = DoubleArray(WIDTH)
    private val FOV = 0.7

    private val textures = arrayOf(
            Texture("img/redbrick.png"),
            Texture("img/eagle.png"),
            Texture("img/mossy.png"),
            Texture("img/greystone.png"),
            Texture("img/barrel.png"),
            Texture("img/pillar.png")
    )

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
        drawSprites(state)
    }

    private fun drawSprites(state: State) {
        val playerpos = state.player.position
        val angle = state.player.angle
        val tasks = ArrayList<Sprite>()
        for (sprite in state.level.sprites) {
            val pos = (sprite.position - playerpos).rotate(-angle)
            if (pos.x > 0) {
                tasks.add(Sprite(pos, sprite.texture))
            }
        }
        tasks.sortBy { -it.position.x }
        for (task in tasks) {
            drawSprite(task.position, task.texture)
        }
    }

    private fun drawSprite(pos: Vec2, textureId: Int) {
        val fovwidth = pos.x * FOV * 2 // how many steps to the side for one step forward
        val px = (pos.y / fovwidth * WIDTH + WIDTH / 2).toInt() // x coordinate on screen

        val depth = depthBuffer[Math.min(Math.max(0, px), WIDTH - 1)]
        if (depth == 0.0) { // sprite hidden by wall
            return
        }
        val height = (HEIGHT / pos.x).toInt()
        val height2 = height / 2

        val xoff = px - height2
        val yoff = HEIGHT / 2 - height2
        val x0 = Math.max(0, xoff)
        val x1 = Math.min(WIDTH - 1, xoff + height)
        val y0 = Math.max(0, yoff)
        val y1 = Math.min(HEIGHT - 1, yoff + height)

        for (x in x0..x1) {
            val u = (x - xoff) / height.toDouble()
            if (x < 0 || x >= WIDTH || depthBuffer[x] < pos.x) {
                continue
            }
            for (y in y0..y1) {
                val v = (y - yoff) / height.toDouble()
                val color = textures[textureId].getPixel(u, v)
                if (color == 0x000000ff) {
                    continue
                }
                setPixel(x, y, color)
            }
        }
    }

    private fun drawWalls(state: State) {
        val pos = state.player.position
        val angle = state.player.angle
        val dir = Vec2(Math.cos(angle), Math.sin(angle))
        val plane = Vec2(Math.cos(angle + Math.PI / 2), Math.sin(angle + Math.PI / 2)) * FOV
        for (x in 0..WIDTH - 1) {
            val cameraX = 2.0 * x / WIDTH - 1
            val ray = dir + plane * cameraX
            val (dist, side, wallX, tile) = raycaster.ray(pos, ray, state.level)
            depthBuffer[x] = dist
            val height = if (dist > 0) (HEIGHT / dist).toInt() else HEIGHT
            val texture = textures[tile.texture]
            val wallstart = HEIGHT / 2 - height / 2
            val y0 = Math.max(0, wallstart)
            val y1 = Math.min(HEIGHT - 1, wallstart + height)
            for (y in y0..y1) {
                val h = (y - wallstart).toDouble() / height
                var color = texture.getPixel(wallX, h)
                color = mulColor(color, 2.0 / (2 + side))
                setPixel(x, y, color)
            }
        }
        buffer.flip()
    }

    private fun setPixel(x: Int, y: Int, color: Int) {
        buffer.putInt((x + y * WIDTH) * 4, color)
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
        for (x in 0..WIDTH - 1) {
            depthBuffer[x] = 0.0
        }
    }

}