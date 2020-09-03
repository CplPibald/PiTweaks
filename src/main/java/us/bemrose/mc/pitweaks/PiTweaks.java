package us.bemrose.mc.pitweaks;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@net.minecraftforge.fml.common.Mod("pitweaks")
public class PiTweaks {

    static org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger();
    
    public PiTweaks() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // registerCommonEvents();
        // DistExecutor.runWhenOn(Dist.CLIENT, () -> MinecraftByExample::registerClientOnlyEvents);

        net.minecraftforge.eventbus.api.IEventBus modBus = net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus();
        for (Tweak t : tweakList) {
            t.register(modBus);
        }
    }

    // @net.minecraftforge.eventbus.api.SubscribeEvent
    private void setup(final net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {

        TweakConfig.loadConfig(net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get().resolve("pitweaks.toml"));
        for (Tweak t : tweakList) {
            t.setup(event);
        }
    }

    static java.util.List<Tweak> tweakList = java.util.Arrays.asList(
        new RepairCostTweak(),
        new FastFurnaceTweak(),
        new SpawnOnCubeTweak(),
        new FoodCapTweak()
        // new BuoyantBoatTweak() // probably not doing this one
    );
}

class Tweak {
    public void register(net.minecraftforge.eventbus.api.IEventBus bus) {}
    public void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {}
    public void clientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event) {}
}