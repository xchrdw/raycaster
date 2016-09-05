package de.chrdw.raycaster.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import java.nio.file.Path

class Texture(file: String) {

    val data: IntArray
    val width: Int
    val height: Int

    init {
        val texture = Texture(Gdx.files.internal(file))
        val textureData = texture.textureData
        textureData.prepare()
        val map = textureData.consumePixmap()
        width = texture.width
        height = texture.height
        data = IntArray(width * height)
        for (i in 0..width * height - 1) {
            data[i] = map.getPixel(i % width, i / width)
        }
        texture.dispose()
    }

    fun getPixel(x: Double, y: Double): Int {
        return data[(x * width).toInt() + (y * height).toInt() * width]
    }

    fun getPixel(x: Int, y: Int): Int {
        return data[x + y * width]
    }

}