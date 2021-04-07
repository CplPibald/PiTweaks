package us.bemrose.mc.pitweaks;

import net.minecraftforge.common.ForgeConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber
public class TweakConfig {

    public static ForgeConfigSpec commonConfig;
    public static ForgeConfigSpec.BooleanValue   anvilNoRepairCost;
    public static ForgeConfigSpec.BooleanValue   anvilOverlevelBooks;
    public static ForgeConfigSpec.BooleanValue   anvilAlwaysAllowBooks;
    public static ForgeConfigSpec.BooleanValue   anvilDisenchant;
    public static ForgeConfigSpec.DoubleValue    anvilBreakChance;
    public static ForgeConfigSpec.BooleanValue   furnaceEnabled;
    public static ForgeConfigSpec.BooleanValue   blastFurnaceEnabled;
    public static ForgeConfigSpec.BooleanValue   smokerEnabled;
    public static ForgeConfigSpec.BooleanValue   campfireEnabled;
    public static ForgeConfigSpec.IntValue       furnaceMultiplier;
    public static ForgeConfigSpec.BooleanValue   brewingEnabled;
    public static ForgeConfigSpec.IntValue       brewingTicks;
    public static ForgeConfigSpec.BooleanValue   spawnFullCubeEnabled;
    public static ForgeConfigSpec.BooleanValue   allowPhantoms;
    public static ForgeConfigSpec.BooleanValue   noInvulnerabilityTicks;
    public static ForgeConfigSpec.BooleanValue   playerUncapFood;
    public static ForgeConfigSpec.BooleanValue   playerUncapSaturation;
    public static ForgeConfigSpec.BooleanValue   instantLeafDecay;

    public static void defineConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Anvil Tweaks").push("anvil");
        anvilNoRepairCost       = builder.comment("Removes additional cost from repairing/enchanting items in an anvil multiple times").define("noRepairCost", true);
        anvilOverlevelBooks     = builder.comment("Allow books to be combined over the enchantment max level").define("allowOverlevelBooks", true);
        anvilAlwaysAllowBooks   = builder.comment("Always allow enchantments from books to be applied in an anvil, regardless of item type or other enchantments").define("alwaysAllowBooks", false);
        anvilDisenchant         = builder.comment("Enables disenchanting items by combining with a vanilla book").define("disenchant", true);
        anvilBreakChance        = builder.comment("Chance for an anvil to be damaged on use (default 0.12)").defineInRange("breakChance", 0.12, 0.0, 1.0);
        builder.pop();

        builder.comment("Vanilla Machine Speed Tweaks").push("machines");
        furnaceEnabled          = builder.comment("Vanilla furnaces are faster.  If you toggle this, you must break and re-place any furnaces.").define("fastFurnace", true);
        blastFurnaceEnabled     = builder.comment("Vanilla blast furnaces are faster.  If you toggle this, you must break and re-place any blast furnaces.").define("fastBlastFurnace", true);
        smokerEnabled           = builder.comment("Vanilla smokers are faster.  If you toggle this, you must break and re-place any smokers.").define("fastSmoker", true);
        campfireEnabled         = builder.comment("Vanilla campfires cook faster.  If you toggle this, you must break and re-place any campfires.").define("fastCampfire", true);
        furnaceMultiplier       = builder.comment("Cook speed multiplier for furnace, blast furnace, and smoker. Time per item is (10/multiplier) seconds. Valid values (2,4,5,8,10,20,25,40,50)").defineInRange("multiplier", 20, 2, 50);
        brewingEnabled          = builder.comment("Brewing stands are faster.  If you toggle this, you must break and re-place any brewing stands.").define("fastBrewing", true);
        brewingTicks            = builder.comment("Number of ticks to brew a potion.  Vanilla = 400.").defineInRange("brewTicks", 20, 2, 400);
        builder.pop();

        builder.comment("Entity Tweaks").push("entity");
        spawnFullCubeEnabled    = builder.comment("Mobs can only spawn blocks with a full-cube collision box.  Prevents spawning on partial blocks like top-slabs, upside-down-stairs, and hoppers.").define("spawnRequiresFullCube", true);
        allowPhantoms           = builder.comment("Set to false to disable phantom spawns.").define("allowPhantoms", true);
        noInvulnerabilityTicks  = builder.comment("Removes 0.5 sec invulnerability time after an entity gets hit.").define("noInvulnerabilityTicks", false);
        builder.pop();

        builder.comment("Hunger Tweaks").push("food");
        playerUncapFood         = builder.comment("Remove food cap that limits food value to 20. Useful with modded foods").define("uncapFood", true);
        playerUncapSaturation   = builder.comment("Remove saturation cap that limits saturation to current food level").define("uncapSaturation", true);
        builder.pop();

        builder.comment("Plant Tweaks").push("plant");
        instantLeafDecay        = builder.comment("Leaves decay immediately when their supporting tree is removed").define("instantLeafDecay", true);
        builder.pop();

    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        defineConfig(builder);
        commonConfig = builder.build();
    }        

    public static void loadConfig(java.nio.file.Path path) {

        CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync().autosave().writingMode(com.electronwill.nightconfig.core.io.WritingMode.REPLACE).build();

        configData.load();
        commonConfig.setConfig(configData);
    }

    // @SubscribeEvent
    // public static void onLoad(final ModConfig.Loading configEvent) {

    // }

    // @SubscribeEvent
    // public static void onReload(final ModConfig.ConfigReloading configEvent) {

    // }

}