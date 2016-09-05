package de.chrdw.raycaster.game

class Player {
    var position = Vec2((LEVELSIZE - 1) / 2.0, (LEVELSIZE - 1) / 2.0)
    var angle = 0.0

    val SPEED = 3.0
    val ANGLE_SPEED = Math.toRadians(180.0)

    fun update(delta: Double, input: Input) {
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
            val move = Vec2(dX, dY).normalized().rotate(angle) * (delta * SPEED)
            val newPos = position + move
            if (newPos.x > 0 && newPos.x < LEVELSIZE && newPos.y > 0 && newPos.y < LEVELSIZE) {
                position = newPos
            }
        }

        if (input.left)
            angle -= delta * ANGLE_SPEED
        if (input.right)
            angle += delta * ANGLE_SPEED

    }

}