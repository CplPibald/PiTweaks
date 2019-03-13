package us.bemrose.mc.pitweaks;

import net.minecraftforge.common.config.Config;

@Config(modid = PiTweaks.MODID)
public class TweakConfig {

    @Config.Comment("Tweaks to anvils")
    public static AnvilTweakConfig anvil = new AnvilTweakConfig();
    public static class AnvilTweakConfig {
        @Config.Comment("Removes additional cost from repairing/enchanting items in an anvil multiple times")
        public boolean noRepairCost = true;

        @Config.Comment("Allow books to be combined over the enchantment max level")
        public boolean allowOverlevelBooks = true;
        
        @Config.Comment("Always allow enchantments from books to be applied in an anvil, regardless of item type or other enchantments")
        public boolean alwaysAllowBooks = false;
    }

    @Config.Comment("Tweaks to vanilla furnaces")
    public static FurnaceTweakConfig furnace = new FurnaceTweakConfig();
    public static class FurnaceTweakConfig {
        @Config.Comment("Vanilla furnaces are faster.  If you toggle this, you must break and re-place any furnaces.")
        public boolean fastFurnace = true;

        @Config.Comment("Cook speed multiplier. Time per item is (10/multiplier) seconds. Valid values (2,4,5,8,10,20,25,40,50,100)")
        @Config.RangeInt(min = 1, max = 100)
        public int multiplier = 20;
    }
    
    @Config.Comment("Tweaks to vanilla brewing stands")
    public static BrewingTweakConfig brewing = new BrewingTweakConfig();
    public static class BrewingTweakConfig {
        @Config.Comment("Brewing stands are faster.  If you toggle this, you must break and re-place any brewing stands.")
        public boolean fastBrewing = true;
        
        @Config.Comment("Number of ticks to brew a potion.  Vanilla = 400.")
        @Config.RangeInt(min = 1, max = 400)
        public int brewTicks = 20;
    }

    @Config.Comment("Tweaks to vanilla boats")
    public static BoatTweakConfig boats = new BoatTweakConfig();
    public static class BoatTweakConfig {
        @Config.Comment("Boats float up flowing water instead of sinking.  If you toggle this, you must break and re-place any boats.")
        public boolean buoyantBoats = true;
        
        @Config.Comment("Disables vanilla behavior of ejecting passengers from a boat after 3 seconds underwater. Requires buoyantBoats = true")
        public boolean dontEjectPassengers = false;
    }
    
    @Config.Comment("Tweaks to recipes")
    public static RecipeTweakConfig recipes = new RecipeTweakConfig();
    public static class RecipeTweakConfig {
        @Config.Comment("Enables 2x2 versions of stair and slab recipes, and adds recipes to recombine them into their base blocks.")
        @Config.RequiresMcRestart
        public boolean recipes2x2 = false;
        
        @Config.Comment("Removes 3x3 version of slab and stair recipes when recipes2x2=true")
        @Config.RequiresMcRestart
        public boolean remove3x3Recipes = false;
        
        @Config.Comment("How to handle conflicts with 2x2 stone and wooden pressure plates when recipes2x2=true. 0 = do nothing (use another recipe mod like minetweaker).  1 = change plate recipes to use slabs.")
        @Config.RequiresMcRestart
        public int pressurePlateHandling = 0;
    }
    
    @Config.Comment("Tweaks to mob spawning")
    public static SpawnTweakConfig spawn = new SpawnTweakConfig();
    public static class SpawnTweakConfig {
        @Config.Comment("Mobs can only spawn on full-cube blocks.  Prevents spawning on partial blocks like top-slabs, upside-down-stairs, and hoppers.")
        public boolean spawnRequiresFullCube = false;
    }

    @net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = PiTweaks.MODID)
	private static class EventHandler {

		// Inject the new values and save to the config file when the config has been changed from the GUI.
		@net.minecraftforge.fml.common.eventhandler.SubscribeEvent
		public static void onConfigChanged(final net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(PiTweaks.MODID)) {
				net.minecraftforge.common.config.ConfigManager.sync(PiTweaks.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
			}
		}
	}    
    
}