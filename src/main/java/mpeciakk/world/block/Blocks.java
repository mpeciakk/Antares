package mpeciakk.world.block;

import mpeciakk.registry.Registry;

public class Blocks {
    public static Block COBBLESTONE;

    public static void init() {
        COBBLESTONE = Registry.register(Registry.BLOCK, "cobblestone", new Block(1));
    }
}
