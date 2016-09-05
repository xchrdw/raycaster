package de.chrdw.raycaster.game

class Vec2(val x: Double, val y: Double) {

    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

    operator fun plus(other: Vec2): Vec2 {
        return Vec2(x + other.x, y + other.y)
    }

    operator fun times(s: Double): Vec2 {
        return Vec2(x * s, y * s)
    }

    operator fun div(s: Double): Vec2 {
        return Vec2(x / s, y / s)
    }

    operator fun div(s: Int): Vec2 {
        return Vec2(x / s, y / s)
    }

    operator fun minus(other: Vec2): Vec2 {
        return Vec2(x - other.x, y - other.y)
    }

    fun rotate(angle: Double): Vec2 {
        val sin = Math.sin(angle)
        val cos = Math.cos(angle)
        return Vec2(cos * x - sin * y, sin * x + cos * y)
    }

    fun length(): Double {
        return Math.sqrt(x * x + y * y)
    }

    fun normalized(): Vec2 {
        val l = length()
        return Vec2(x / l, y / l)
    }

    override fun toString(): String {
        return "{$x, $y}"
    }

}