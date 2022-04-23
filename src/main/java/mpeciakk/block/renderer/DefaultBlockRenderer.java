package mpeciakk.block.renderer;

import mpeciakk.asset.data.Texture;
import mpeciakk.block.Block;
import mpeciakk.block.state.BlockState;
import mpeciakk.debug.DebugTools;
import mpeciakk.model.Model;
import mpeciakk.model.ModelPart;
import mpeciakk.render.mesh.builder.ComplexMeshBuilder;
import mpeciakk.render.mesh.builder.MeshBuilder;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.util.BlockPos;
import mpeciakk.util.Direction;
import mpeciakk.world.World;
import org.joml.Vector3f;

public class DefaultBlockRenderer extends BlockRenderer {

    public DefaultBlockRenderer(Block block) {
        super(block);
    }

    @Override
    public void render(MeshBuilder<?> meshBuilder, BlockState state, Model model, World world, BlockPos localPosition, BlockPos position) {
        if (model.getType().equals("complex")) {
            ComplexMeshBuilder complexMeshBuilder = ((ComplexMeshBuilder) meshBuilder);

            for (ModelPart part : model.getParts()) {
                try {
                    part.draw(complexMeshBuilder, new Vector3f(position.asVector()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            SimpleMeshBuilder simpleMeshBuilder = ((SimpleMeshBuilder) meshBuilder);

            BlockState northBlock = world.getBlock(new BlockPos(position).offset(Direction.NORTH));
            boolean renderNorth = northBlock == null || !northBlock.getModel().getType().equals("full");

            BlockState southBlock = world.getBlock(new BlockPos(position).offset(Direction.SOUTH));
            boolean renderSouth = southBlock == null || !southBlock.getModel().getType().equals("full");

            BlockState eastBlock = world.getBlock(new BlockPos(position).offset(Direction.EAST));
            boolean renderEast = eastBlock == null || !eastBlock.getModel().getType().equals("full");

            BlockState westBlock = world.getBlock(new BlockPos(position).offset(Direction.WEST));
            boolean renderWest = westBlock == null || !westBlock.getModel().getType().equals("full");

            BlockState upBlock = world.getBlock(new BlockPos(position).offset(Direction.UP));
            boolean renderUp = upBlock == null || !upBlock.getModel().getType().equals("full");

            BlockState downBlock = world.getBlock(new BlockPos(position).offset(Direction.DOWN));
            boolean renderDown = downBlock == null || !downBlock.getModel().getType().equals("full");

            Texture front = model.getTextures().get("front");
            Texture back = model.getTextures().get("back");
            Texture left = model.getTextures().get("left");
            Texture right = model.getTextures().get("right");
            Texture bottom = model.getTextures().get("bottom");
            Texture top = model.getTextures().get("top");

            if (DebugTools.naive) {
                simpleMeshBuilder.drawCuboid(localPosition.getX(), localPosition.getY(), localPosition.getZ(), 1, 1, 1, front, back, left, right, bottom, top, renderNorth, renderSouth, renderEast, renderWest, renderDown, renderUp);
            } else {
                simpleMeshBuilder.drawCuboid(localPosition.getX(), localPosition.getY(), localPosition.getZ(), 1, 1, 1, front, back, left, right, bottom, top, true, true, true, true, true, true);
            }
        }
    }
}
