package mpeciakk.world.block;

import mpeciakk.registry.Registry;

public class Blocks {
    public static Block AIR;
    public static Block COBBLESTONE;

    public static void init() {
        AIR = Registry.BLOCK.register("air", new Block());
        COBBLESTONE = Registry.BLOCK.register("cobblestone", new Block());
    }
}
