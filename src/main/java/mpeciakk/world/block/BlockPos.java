package mpeciakk.world.block;

import mpeciakk.util.Direction;
import org.joml.Vector3i;

public class BlockPos {
    private final Vector3i pos;

    public BlockPos(int x, int y, int z) {
        this.pos = new Vector3i(x, y, z);
    }

    public BlockPos offset(Direction direction) {
        pos.add(direction.getOffset());

        return this;
    }

    public Vector3i asVector() {
        return pos;
    }

    public int getX() {
        return pos.x;
    }

    public int getY() {
        return pos.y;
    }

    public int getZ() {
        return pos.z;
    }
}
