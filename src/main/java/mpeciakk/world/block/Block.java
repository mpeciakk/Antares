package mpeciakk.world.block;

public class Block {

    private boolean full;
    private BlockModel model;

    public Block(boolean full) {
        this.full = full;
    }

    public boolean isFull() {
        return full;
    }

    public BlockModel getModel() {
        return model;
    }

    public void setModel(BlockModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Block{" +
                "full=" + full +
                ", model=" + model +
                '}';
    }
}