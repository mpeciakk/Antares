package mpeciakk.block.renderer;

import mpeciakk.asset.data.Texture;
import mpeciakk.block.Block;
import mpeciakk.block.state.BlockState;
import mpeciakk.debug.DebugTools;
import mpeciakk.model.block.BlockModel;
import mpeciakk.render.mesh.builder.MeshBuilder;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.util.BlockPos;
import mpeciakk.util.Direction;
import mpeciakk.world.World;

public class DefaultBlockRenderer extends BlockRenderer {

    public DefaultBlockRenderer(Block block) {
        super(block);
    }

    @Override
    public void render(MeshBuilder<?> meshBuilder, BlockState state, BlockModel model, World world, BlockPos localPosition, BlockPos position) {
        if (model.isComplex()) {

        } else {
            SimpleMeshBuilder simpleMeshBuilder = ((SimpleMeshBuilder) meshBuilder);

            System.out.println(position);

            BlockState northBlock = world.getBlock(new BlockPos(position).offset(Direction.NORTH));
            BlockState southBlock = world.getBlock(new BlockPos(position).offset(Direction.SOUTH));
            BlockState eastBlock = world.getBlock(new BlockPos(position).offset(Direction.EAST));
            BlockState westBlock = world.getBlock(new BlockPos(position).offset(Direction.WEST));
            BlockState upBlock = world.getBlock(new BlockPos(position).offset(Direction.UP));
            BlockState downBlock = world.getBlock(new BlockPos(position).offset(Direction.DOWN));

            System.out.println(position);

            Texture front = model.getTextures().get("front");
            Texture back = model.getTextures().get("back");
            Texture left = model.getTextures().get("left");
            Texture right = model.getTextures().get("right");
            Texture bottom = model.getTextures().get("bottom");
            Texture top = model.getTextures().get("top");

            if (DebugTools.naive) {
                simpleMeshBuilder.drawCuboid(localPosition.getX(), localPosition.getY(), localPosition.getZ(), 1, 1, 1, front, back, left, right, bottom, top, northBlock == null || !northBlock.getModel().isFull(), southBlock == null || !southBlock.getModel().isFull(), eastBlock == null || !eastBlock.getModel().isFull(), westBlock == null || !westBlock.getModel().isFull(), downBlock == null || !downBlock.getModel().isFull(), upBlock == null || !upBlock.getModel().isFull());
            } else {
                simpleMeshBuilder.drawCuboid(localPosition.getX(), localPosition.getY(), localPosition.getZ(), 1, 1, 1, front, back, left, right, bottom, top, true, true, true, true, true, true);
            }
        }
    }
}
