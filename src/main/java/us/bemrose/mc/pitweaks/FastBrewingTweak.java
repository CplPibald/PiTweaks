package us.bemrose.mc.pitweaks;

public class FastBrewingTweak extends Tweak {

    public static int maxBrewTicks = 20;

    public FastBrewingTweak(int ticks) {
        maxBrewTicks = ticks;
    }

    @Override
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        // Register block place event
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
        // Register new tile entity
        net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(TileEntityFastBrewingStand.class, "PiTweaksFastBrewingStand");
    }

    // BlockEvent
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onBlockPlaced(net.minecraftforge.event.world.BlockEvent.PlaceEvent event) {

        // When a stand is placed, replace its tile entity with our own
        if (event.getPlacedBlock().getBlock() == net.minecraft.init.Blocks.BREWING_STAND) {
            event.getWorld().setTileEntity(event.getBlockSnapshot().getPos(), new TileEntityFastBrewingStand());
        }
    }
}
