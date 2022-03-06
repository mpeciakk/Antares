package mpeciakk.world.block;

public class Block {

    private int type;
    private BlockModel model;

    public Block(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
                "type=" + type +
                '}';
    }
}