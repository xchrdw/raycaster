package de.chrdw.raycaster.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Input {
    var forward: Boolean = false
    var backward: Boolean = false
    var left: Boolean = false
    var right: Boolean = false
    var strafeLeft: Boolean = false
    var strafeRight: Boolean = false


    fun update() {
        forward = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)
        backward = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)
        left = Gdx.input.isKeyPressed(Input.Keys.LEFT)
        right = Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        strafeLeft = Gdx.input.isKeyPressed(Input.Keys.A)
        strafeRight = Gdx.input.isKeyPressed(Input.Keys.D)
    }
}