package de.chrdw.raycaster.game

class Player {
    var position = Vec2((LEVELSIZE - 1) / 2.0 + 0.5, (LEVELSIZE - 1) / 2.0 + 0.5)
    var angle = 0.0

    val SPEED = 3.0
    val ANGLE_SPEED = Math.toRadians(120.0)

    fun update(delta: Double, input: Input, state: State) {
        var dX = 0.0
        var dY = 0.0

        if (input.forward)
            dX = 1.0
        if (input.backward)
            dX = -1.0
        if (input.strafeLeft)
            dY = -1.0
        if (input.strafeRight)
            dY = 1.0

        if (dX != 0.0 || dY != 0.0) {
            val dir = Vec2(dX, dY).normalized().rotate(angle)
            val move = dir * (delta * SPEED)

            if (state.level.canWalk(position + move)) {
                position += move
            } else if (state.level.canWalk(position + Vec2(move.x, 0.0))) {
                position += Vec2(move.x, 0.0)
            } else if (state.level.canWalk(position + Vec2(0.0, move.y))) {
                position += Vec2(0.0, move.y)
            }

        }

        if (input.left)
            angle -= delta * ANGLE_SPEED
        if (input.right)
            angle += delta * ANGLE_SPEED

    }
}