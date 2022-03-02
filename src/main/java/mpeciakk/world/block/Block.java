package mpeciakk.world.block;

public class Block {

    private int type;

    public Block(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Block{" +
                "type=" + type +
                '}';
    }
}