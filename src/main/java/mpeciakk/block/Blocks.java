package mpeciakk.block;

import mpeciakk.registry.Registry;

public class Blocks {
    public static Block AIR;
    public static Block COBBLESTONE;
    public static Block TEST_BLOCK;

    public static void init() {
        AIR = Registry.BLOCK.register("air", new Block());
        COBBLESTONE = Registry.BLOCK.register("cobblestone", new Block());
        TEST_BLOCK = Registry.BLOCK.register("test_block", new TestBlock());
    }
}
