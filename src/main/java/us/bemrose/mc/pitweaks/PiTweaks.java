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

        if (cfg.getBoolean("enabled", "norepaircost", false, "Removes cumulative repair cost from anvils.")) {
            boolean allowOverlevelBooks = cfg.getBoolean("allowOverlevelBooks", "norepaircost", true, "Allow books to be combined over the enchantment max level'");
            boolean alwaysAllowBooks = cfg.getBoolean("alwaysAllowBooks", "norepaircost", true, "Always allow enchantments from books to be applied in an anvil, even if the enchantment doesn't make sense for the item'");
            tweaks.add(new RepairCostTweak(true, allowOverlevelBooks, alwaysAllowBooks));
        }

        if (cfg.getBoolean("enabled", "fastfurnace", false, "Vanilla furnaces are faster.")) {
            int mult = cfg.getInt("multiplier", "fastfurnace", 20, 1, 200, "Cook speed multiplier. Time per item is (10/multiplier) seconds. Value must be a factor of 200. Valid values (2,4,5,8,10,20,25,40,50,100)");
            tweaks.add(new FastFurnaceTweak(mult));
        }

        if (cfg.getBoolean("enabled", "fastbrewing", false, "Brewing stands are faster.")) {
            int time = cfg.getInt("brewTicks", "fastbrewing", 20, 1, 400, "Number of ticks to brew a potion.  Vanilla = 400.");
            tweaks.add(new FastBrewingTweak(time));
        }

        if (cfg.getBoolean("enabled", "buoyantboats", false, "Boats float up flowing water instead of sinking.")) {
            boolean dontEject = cfg.getBoolean("dontEjectPassengers", "buoyantboats", false, "Disables vanilla behavior of ejecting passengers from a boat after 3 seconds underwater.");
            tweaks.add(new BuoyantBoatTweak(dontEject));
        }

        if (cfg.getBoolean("enabled", "2x2-recipes", false, "Enables 2x2 versions of stair and slab recipes, and adds recipes to recombine them into their base blocks.")) {
            boolean removeOld = cfg.getBoolean("removeOldRecipes", "2x2-recipes", true, "Removes 3x3 version of slab and stair recipes");
            int doPlates = cfg.getInt("pressurePlates", "2x2-recipes", 1, 0, 1, "How to handle 2x2 stone and wooden pressure plate recipes. 0 = do nothing (use another recipe mod like minetweaker).  1 = change recipes to 2x1 slabs.");
            tweaks.add(new StairSlabTweak(doPlates, removeOld));
        }

        if (cfg.getBoolean("enabled", "spawnrequiresfullcube", false, "Mobs can only spawn on full-cube blocks.  Prevents spawning on partial blocks like slabs, stairs, and hoppers.")) {
            tweaks.add(new SpawnOnCubeTweak());
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