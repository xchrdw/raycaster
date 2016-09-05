package de.chrdw.raycaster

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import de.chrdw.raycaster.game.HEIGHT
import de.chrdw.raycaster.game.Game
import de.chrdw.raycaster.game.WIDTH

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.title = "Wolf"
    config.width = WIDTH
    config.height = HEIGHT
    config.vSyncEnabled = false
    LwjglApplication(Game(), config)
}

