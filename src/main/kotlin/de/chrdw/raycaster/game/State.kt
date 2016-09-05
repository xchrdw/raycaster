package de.chrdw.raycaster.game

class State {
    val level = Level()
    val player = Player()

    init {
        player.position = level.start
    }

    fun update(delta: Double, input: Input) {
        player.update(delta, input)
    }
}