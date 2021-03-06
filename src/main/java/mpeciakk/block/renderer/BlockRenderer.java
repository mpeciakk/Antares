package mpeciakk.block.renderer;

import mpeciakk.block.Block;
import mpeciakk.block.state.BlockState;
import mpeciakk.model.Model;
import mpeciakk.render.mesh.builder.MeshBuilder;
import mpeciakk.util.BlockPos;
import mpeciakk.world.World;

public abstract class BlockRenderer {

    protected final Block block;

    public BlockRenderer(Block block) {
        this.block = block;
    }

    public abstract void render(MeshBuilder<?> meshBuilder, BlockState state, Model model, World world, BlockPos localPosition, BlockPos position);
}
