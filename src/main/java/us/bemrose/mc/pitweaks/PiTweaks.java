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
    }

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        for (Tweak t : loadedTweaks) {
            t.postInit(event);
        }
    }

    static java.util.List<Tweak> getTweakList(Configuration cfg) {
        java.util.List<Tweak> tweaks = new java.util.LinkedList<Tweak>();
        tweaks.add(new RepairCostTweak());
        tweaks.add(new FastFurnaceTweak());
        tweaks.add(new FastBrewingTweak());
        tweaks.add(new BuoyantBoatTweak());
        tweaks.add(new StairSlabTweak());
        tweaks.add(new SpawnOnCubeTweak());
        tweaks.add(new FoodCapTweak());
        
        return tweaks;
    }

//    System.out.println("DIRT BLOCK >> " + net.minecraft.init.Blocks.DIRT.getUnlocalizedName());
}

class Tweak {
    public void preInit(FMLPreInitializationEvent event) {};
    public void init(FMLInitializationEvent event) {};
    public void postInit(FMLPostInitializationEvent event) {};
}