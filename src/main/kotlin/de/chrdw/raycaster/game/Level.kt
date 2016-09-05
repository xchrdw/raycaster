package de.chrdw.raycaster.game

import com.badlogic.gdx.graphics.Color


enum class TileType() {
    FLOOR,
    WALL,
    START
}

val level = """
xxxxxxxxxxxxxxxxxxxx
x        xx        x
x        xx        x
x        xx  g g g x
x      b xx        x
x   p        g g g x
x      r xx        x
x        xx  g g g x
x        xx        x
xxxex xexxxxxx xxxxx
xxxxx xxxxxxxx xxxxx
x        xx        x
x        xx        x
x        xx        x
x  rrr   xx    b   x
x  rrr        bbb  x
x  rrr   xx    b   x
x        xx        x
x        xx        x
xxxxxxxxxxxxxxxxxxxx
""".replace("""\n""".toRegex(), "")

data class Tile(val type: TileType, val texture: Int)

class Level {

    val data: Array<Tile>

    var start: Vec2 = Vec2(LEVELSIZE/2, LEVELSIZE/2)

    init {
        assert(level.length == LEVELSIZE* LEVELSIZE)
        data = Array(LEVELSIZE * LEVELSIZE, { i ->
            when (level[i]) {
                ' ' -> Tile(TileType.FLOOR, -1)
                'r' -> Tile(TileType.WALL, 0)
                'e' -> Tile(TileType.WALL, 1)
                'g' -> Tile(TileType.WALL, 2)
                'b' -> Tile(TileType.WALL, 3)
                'p' -> {
                    start = Vec2(i % LEVELSIZE, i / LEVELSIZE)
                    Tile(TileType.FLOOR, -1)
                }
                else -> Tile(TileType.WALL, 0)
            }
        })
    }

    fun get(x: Int, y: Int): Tile {
        if (x < 0 || x >= LEVELSIZE || y < 0 || y >= LEVELSIZE) {
            return Tile(TileType.WALL, Color.rgba8888(1f, 1f, 1f, 1f))
        }
        return data[x + LEVELSIZE * y]
    }

}