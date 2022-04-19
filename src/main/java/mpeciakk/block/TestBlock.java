package mpeciakk.block;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.data.Texture;
import mpeciakk.block.property.BooleanProperty;
import mpeciakk.block.property.IntProperty;
import mpeciakk.block.renderer.BlockRenderer;
import mpeciakk.block.state.BlockState;
import mpeciakk.model.block.BlockModel;
import mpeciakk.render.mesh.builder.MeshBuilder;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.util.BlockPos;
import mpeciakk.world.World;
import org.joml.Vector3i;

public class TestBlock extends Block {
    public static final BooleanProperty property1 = new BooleanProperty("property1");
    public static final IntProperty property2 = new IntProperty("property2");
    public static final BooleanProperty property3 = new BooleanProperty("property3");

    @Override
    protected void appendProperties() {
        stateManager.addProperty(property1).addProperty(property2).addProperty(property3);
    }
}
