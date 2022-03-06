package mpeciakk.util;

import org.joml.Vector3i;

public enum Direction {
    NONE(0, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(1, 0, 0),
    EAST(-1, 0, 0);

    private static final Direction[] ALL = Direction.values();

    private final Vector3i offset;

    Direction(int x, int y, int z) {
        this.offset = new Vector3i(x, y, z);
    }

    public static Direction of(Vector3i vector) {
        for (Direction direction : ALL) {
            if (direction.offset.equals(vector)) {
                return direction;
            }
        }

        return Direction.NONE;
    }

    public Vector3i getOffset() {
        return offset;
    }
}
