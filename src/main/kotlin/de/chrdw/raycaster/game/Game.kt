package de.chrdw.raycaster.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch

const val WIDTH = 800
const val HEIGHT = 600

const val FLOOR_COLOR = 0x3f3f3fff
const val CEILING_COLOR = 0x9f9f9fff.toInt()

const val LEVELSIZE = 20


class Game : ApplicationAdapter() {

    private lateinit var renderer: Renderer
    private lateinit var batch: SpriteBatch

    private val state = State()
    private val input = Input()

    override fun create() {
        batch = SpriteBatch()
        renderer = Renderer()
    }

    override fun render() {
        val delta = Gdx.graphics.deltaTime

        if(delta > 1.0/50)
            println(delta)
        input.update()
        state.update(delta.toDouble(), input)

        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.draw(state)
        val texture = renderer.texture()

        batch.begin()
        batch.draw(texture, 0f, 0f)
        batch.end()
        texture.dispose()
    }
}