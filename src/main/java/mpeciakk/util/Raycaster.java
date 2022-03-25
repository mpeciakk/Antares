package mpeciakk.util;

import mpeciakk.world.World;
import mpeciakk.block.Block;
import mpeciakk.block.BlockPos;
import mpeciakk.block.Blocks;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Raycaster {

    private final Vector3f hitPos = new Vector3f();
    private final Vector3i face = new Vector3i();
    private final BlockPos blockPos = new BlockPos();

    public RaycastHit ray(World world, Vector3f origin, Vector3f direction, float maxDistance) {
        float px = origin.x;
        float py = origin.y;
        float pz = origin.z;

        float dx = direction.x;
        float dy = direction.y;
        float dz = direction.z;

        float ds = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

        dx /= ds;
        dy /= ds;
        dz /= ds;

        float t = 0.0f;
        float ix = (float) Math.floor(px);
        float iy = (float) Math.floor(py);
        float iz = (float) Math.floor(pz);
        int stepx = (dx > 0) ? 1 : -1;
        int stepy = (dy > 0) ? 1 : -1;
        int stepz = (dz > 0) ? 1 : -1;

        float txDelta = Math.abs(1 / dx);
        float tyDelta = Math.abs(1 / dy);
        float tzDelta = Math.abs(1 / dz);

        float xdist = (stepx > 0) ? (ix + 1 - px) : (px - ix);
        float ydist = (stepy > 0) ? (iy + 1 - py) : (py - iy);
        float zdist = (stepz > 0) ? (iz + 1 - pz) : (pz - iz);

        float txMax = (txDelta < Float.MAX_VALUE) ? txDelta * xdist : Float.MAX_VALUE;
        float tyMax = (tyDelta < Float.MAX_VALUE) ? tyDelta * ydist : Float.MAX_VALUE;
        float tzMax = (tzDelta < Float.MAX_VALUE) ? tzDelta * zdist : Float.MAX_VALUE;

        int steppedIndex = -1;

        hitPos.x = 0;
        hitPos.y = 0;
        hitPos.z = 0;

        face.x = 0;
        face.y = 0;
        face.z = 0;

        while (t <= maxDistance) {
            blockPos.set((int) ix, (int) iy, (int) iz);
            Block block = world.getBlock(blockPos);

            if (block != null && block != Blocks.AIR) {
                hitPos.x = px + t * dx;
                hitPos.y = py + t * dy;
                hitPos.z = pz + t * dz;

                if (steppedIndex == 0) face.x = -stepx;
                if (steppedIndex == 1) face.y = -stepy;
                if (steppedIndex == 2) face.z = -stepz;

                return new RaycastHit(block, hitPos, blockPos, Direction.of(face));
            }

            if (txMax < tyMax) {
                if (txMax < tzMax) {
                    ix += stepx;
                    t = txMax;
                    txMax += txDelta;
                    steppedIndex = 0;
                } else {
                    iz += stepz;
                    t = tzMax;
                    tzMax += tzDelta;
                    steppedIndex = 2;
                }
            } else {
                if (tyMax < tzMax) {
                    iy += stepy;
                    t = tyMax;
                    tyMax += tyDelta;
                    steppedIndex = 1;
                } else {
                    iz += stepz;
                    t = tzMax;
                    tzMax += tzDelta;
                    steppedIndex = 2;
                }
            }
        }

        hitPos.x = px + t * dx;
        hitPos.y = py + t * dy;
        hitPos.z = pz + t * dz;

        return null;
    }

    public record RaycastHit(Block block, Vector3f pos, BlockPos blockPos, Direction face) {
    }
}
