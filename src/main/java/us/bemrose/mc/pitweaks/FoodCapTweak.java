package us.bemrose.mc.pitweaks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.nbt.CompoundNBT;
import java.lang.reflect.Field;

public class FoodCapTweak extends Tweak {

    @Override
    public void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {

        // Disable this tweak if AppleCore is loaded, because it overloads foodstats
        // TODO: Use AppleCore API when loaded.
        if (net.minecraftforge.fml.ModList.get().isLoaded("applecore")) {
            if (TweakConfig.playerUncapFood.get() || TweakConfig.playerUncapSaturation.get()) { 
                PiTweaks.LOGGER.warn("AppleCore detected.  Disabling food uncapper tweak.");
            }
            return;
        }

        // Register block place event
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onEntityJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {
    
        if (!TweakConfig.playerUncapFood.get() && !TweakConfig.playerUncapSaturation.get()) { return; }
        
        if (event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            FoodStats oldStats = player.getFoodData();

            if (null != oldStats && !(oldStats instanceof TweakFoodStats)) {
                player.foodData = new TweakFoodStats(oldStats);
            }
        }
    }
    
    public class TweakFoodStats extends net.minecraft.util.FoodStats {

        public TweakFoodStats(FoodStats fsIn) {
            CompoundNBT nbt = new CompoundNBT();
            fsIn.addAdditionalSaveData(nbt);
            readAdditionalSaveData(nbt);
        }

        @Override
        public void eat(int foodLevelIn, float foodSaturationModifier) {

            foodLevel += foodLevelIn;
            saturationLevel += (float)foodLevelIn * foodSaturationModifier * 2.0F;
                
            if (!TweakConfig.playerUncapFood.get()) {
                foodLevel = Math.min(foodLevel, 20);
            }
            if (!TweakConfig.playerUncapSaturation.get()) {
                saturationLevel = Math.min(saturationLevel, foodLevel);
            }
        }
    }
}
