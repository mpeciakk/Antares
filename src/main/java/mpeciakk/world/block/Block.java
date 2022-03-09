package mpeciakk.world.block;

public class Block {
    private BlockModel model;

    public BlockModel getModel() {
        return model;
    }

    public void setModel(BlockModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Block{" +
                ", model=" + model +
                '}';
    }
}