package de.chrdw.raycaster.game

import java.util.*


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
x   p    a   g g g x
x      r xx        x
x        xx  g g g x
x a o o  xx        x
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

data class Sprite(val position: Vec2, val texture: Int)


class Level {

    val tiles: Array<Tile>

    val sprites: MutableList<Sprite>

    var start: Vec2 = Vec2(LEVELSIZE / 2 + 0.5, LEVELSIZE / 2 + 0.5)

    init {
        assert(level.length == LEVELSIZE * LEVELSIZE)
        sprites = ArrayList()
        tiles = Array(LEVELSIZE * LEVELSIZE, { i ->
            val position = Vec2(i % LEVELSIZE + 0.5, i / LEVELSIZE + 0.5)
            when (level[i]) {
                ' ' -> Tile(TileType.FLOOR, Textures.Invalid.id)
                'r' -> Tile(TileType.WALL, Textures.RedBrick.id)
                'e' -> Tile(TileType.WALL, Textures.Eagle.id)
                'g' -> Tile(TileType.WALL, Textures.Mossy.id)
                'b' -> Tile(TileType.WALL, Textures.GreyStone.id)
                'a' -> {
                    sprites.add(Sprite(position, Textures.Barrel.id))
                    Tile(TileType.FLOOR, Textures.Invalid.id)
                }
                'o' -> {
                    sprites.add(Sprite(position, Textures.Pillar.id))
                    Tile(TileType.FLOOR, Textures.Invalid.id)
                }
                'p' -> {
                    start = position
                    Tile(TileType.FLOOR, Textures.Invalid.id)
                }
                else -> Tile(TileType.WALL, Textures.GreyStone.id)
            }
        })
    }

    fun get(x: Int, y: Int): Tile {
        if (x < 0 || x >= LEVELSIZE || y < 0 || y >= LEVELSIZE) {
            return Tile(TileType.WALL, 0)
        }
        return tiles[x + LEVELSIZE * y]
    }

    fun  canWalk(pos: Vec2): Boolean {
        val tile = get(pos.x.toInt(), pos.y.toInt())
        return tile.type != TileType.WALL
    }

}