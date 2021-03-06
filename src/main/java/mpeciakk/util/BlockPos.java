package mpeciakk.util;

import org.joml.Vector3i;

public class BlockPos {
    private final Vector3i pos;

    public BlockPos(int x, int y, int z) {
        this.pos = new Vector3i(x, y, z);
    }

    public BlockPos(BlockPos blockPos) {
        this.pos = new Vector3i(blockPos.pos);
    }

    public BlockPos() {
        this(0, 0, 0);
    }

    public void set(int x, int y, int z) {
        pos.x = x;
        pos.y = y;
        pos.z = z;
    }

    public BlockPos offset(Direction direction) {
        pos.add(direction.getOffset());

        return this;
    }

    public BlockPos add(BlockPos blockPos) {
        pos.add(blockPos.pos);

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

    @Override
    public String toString() {
        return "BlockPos{" +
                "pos=" + pos +
                '}';
    }
}
