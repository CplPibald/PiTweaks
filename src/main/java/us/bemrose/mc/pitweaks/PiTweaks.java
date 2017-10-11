package us.bemrose.mc.pitweaks;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.config.Configuration;

@net.minecraftforge.fml.common.Mod(modid = PiTweaks.MODID, version = PiTweaks.VERSION)
public class PiTweaks {
    public static final String MODID = "pitweaks";
    public static final String VERSION = "BUILD_VERSION";

    java.util.List<Tweak> loadedTweaks;
    
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        loadedTweaks = getTweakList(config);

        for (Tweak t : loadedTweaks) {
            t.preInit(event);
        }
        
        if (config.hasChanged()) {
            config.save();
        }
    }

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        for (Tweak t : loadedTweaks) {
            t.init(event);
        }

        // net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new PickupCheat());
        // net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new PumpkinPlaceTweak());
    }

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        for (Tweak t : loadedTweaks) {
            t.postInit(event);
        }
    }

    static java.util.List<Tweak> getTweakList(Configuration cfg) {
        java.util.List<Tweak> tweaks = new java.util.LinkedList<Tweak>();

        if (cfg.getBoolean("enabled", "norepaircost", false, "Removes cumulative repair cost from anvils.")) {
            tweaks.add(new RepairCostTweak());
        }

        if (cfg.getBoolean("enabled", "fastfurnace", false, "Vanilla furnaces are 20x faster.")) {
            tweaks.add(new FastFurnaceTweak());
        }

        if (cfg.getBoolean("enabled", "buoyantboats", false, "Boats float up flowing water instead of sinking.")) {
            boolean dontEject = cfg.getBoolean("dontEjectPassengers", "buoyantboats", false, "Disables vanilla behavior of ejecting passengers from a boat after 3 seconds underwater.");
            tweaks.add(new BuoyantBoatTweak(dontEject));
        }

        if (cfg.getBoolean("enabled", "2x2-recipes", false, "Enables 2x2 versions of stair and slab recipes, and adds recipes to recombine them into their base blocks.")) {
            tweaks.add(new StairSlabTweak());
        }


        return tweaks;
    }

//    System.out.println("DIRT BLOCK >> " + net.minecraft.init.Blocks.DIRT.getUnlocalizedName());
}

class Tweak {
    public void preInit(FMLPreInitializationEvent event) {};
    public void init(FMLInitializationEvent event) {};
    public void postInit(FMLPostInitializationEvent event) {};
}