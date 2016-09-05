package de.chrdw.raycaster.game


class RayCaster {

    data class RayHit(val depth: Double, val side: Int, val wallPart: Double, val tile: Tile)

    fun ray(pos: Vec2, dir: Vec2, level: Level): RayHit {
        var mapX = pos.x.toInt()
        var mapY = pos.y.toInt()
        val deltaDistX = Math.sqrt(1 + dir.y * dir.y / (dir.x * dir.x))
        val deltaDistY = Math.sqrt(1 + dir.x * dir.x / (dir.y * dir.y))
        val stepX: Int
        var sideDistX: Double
        if (dir.x < 0) {
            stepX = -1;
            sideDistX = (pos.x - mapX) * deltaDistX;
        } else {
            stepX = 1;
            sideDistX = (mapX + 1.0 - pos.x) * deltaDistX;
        }
        val stepY: Int
        var sideDistY: Double
        if (dir.y < 0) {
            stepY = -1;
            sideDistY = (pos.y - mapY) * deltaDistY;
        } else {
            stepY = 1;
            sideDistY = (mapY + 1.0 - pos.y) * deltaDistY;
        }

        var side: Int = 0
        var tile: Tile = Tile(TileType.FLOOR, 0)
        //perform DDA
        while (tile.type == TileType.FLOOR) {
            //jump to next map square, OR in x-direction, OR in y-direction
            if (sideDistX < sideDistY) {
                sideDistX += deltaDistX;
                mapX += stepX;
                side = 0;
            } else {
                sideDistY += deltaDistY;
                mapY += stepY;
                side = 1;
            }
            //Check if ray has hit a wall
            tile = level.get(mapX, mapY)
        }

        //Calculate distance projected on camera direction (oblique distance will give fisheye effect!)
        val perpWallDist =
                if (side == 0)
                    (mapX - pos.x + (1 - stepX) / 2) / dir.x;
                else
                    (mapY - pos.y + (1 - stepY) / 2) / dir.y;

        var wallX =
                if (side == 0)
                    pos.y + perpWallDist * dir.y
                else
                    pos.x + perpWallDist * dir.x
        wallX -= Math.floor(wallX)

        if (side == 0 && stepX == -1) wallX = 1 - wallX
        if (side == 1 && stepY == 1) wallX = 1 - wallX


        return RayHit(perpWallDist, side, wallX, tile)
    }

}