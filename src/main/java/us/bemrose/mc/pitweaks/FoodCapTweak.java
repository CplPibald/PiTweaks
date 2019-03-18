package us.bemrose.mc.pitweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.nbt.NBTTagCompound;
import java.lang.reflect.Field;

public class FoodCapTweak extends Tweak {

    @Override
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {

        // Disable this tweak if AppleCore is loaded, because it overloads foodstats
        // TODO: Use AppleCore API when loaded.
        if (net.minecraftforge.fml.common.Loader.isModLoaded("applecore")) {
            if (TweakConfig.player.uncapFood || TweakConfig.player.uncapSaturation) { 
                PiTweaks.LOGGER.warn("AppleCore detected.  Disabling food uncapper tweak.");
            }
            return;
        }

        // Register block place event
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onEntityJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {
    
        if (!TweakConfig.player.uncapFood && !TweakConfig.player.uncapSaturation) { return; }
        
        if (event.getEntity() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.getEntity();
            FoodStats oldStats = player.getFoodStats();

            if (null != oldStats && !(oldStats instanceof TweakFoodStats)) {
                FoodStats newStats = new TweakFoodStats();
                NBTTagCompound foodnbt = new NBTTagCompound();
                oldStats.writeNBT(foodnbt);
                newStats.readNBT(foodnbt);

                try {
                    // FRAGILE: Update name for new MC versions
                    Field foodStats_field = EntityPlayer.class.getDeclaredField("field_71100_bB");
                    foodStats_field.setAccessible(true);
                    foodStats_field.set(player, newStats);
                } catch(IllegalAccessException e) {
                    System.out.println("Can't set foodStats. This should not have happened.");
                } catch(NoSuchFieldException e) {
                    System.out.println("Unable to find foodStats field on player!");
                }
            }
        }
    }
    
    public class TweakFoodStats extends net.minecraft.util.FoodStats {
    
        final int foodLevel_field = 0;
        final int foodSat_field = 1;

        Field[] fields = getClass().getSuperclass().getDeclaredFields();
        
        TweakFoodStats() {
            fields[foodLevel_field].setAccessible(true);
            fields[foodSat_field].setAccessible(true);
        }
        
        
        @Override
        public void addStats(int foodLevelIn, float foodSaturationModifier) {

            try {

                int food = (int)fields[foodLevel_field].get(this);
                float sat = (float)fields[foodSat_field].get(this);

                food += foodLevelIn;
                sat += (float)foodLevelIn * foodSaturationModifier * 2.0F;
                
                if (!TweakConfig.player.uncapFood) {
                    food = Math.min(food, 20);
                }
                if (!TweakConfig.player.uncapSaturation) {
                    sat = Math.min(sat, food);
                }
                
                fields[foodLevel_field].setInt(this, food);
                fields[foodSat_field].setFloat(this, sat);

            } catch(IllegalAccessException e) {
                // We'll never get this exception because we set the accessibility
                // but Java's checked exceptions want me to type this
                // boilerplate anyway, because I'm just the programmer,
                // what the hell do I know?
                System.out.println("This didn't happen.");
            }

        }
    }
}
