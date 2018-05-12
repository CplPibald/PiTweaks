package us.bemrose.mc.pitweaks;

import net.minecraft.item.ItemStack;

public class RepairCostTweak extends Tweak {

    @Override
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        // Register for anvil events
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    // AnvilUpdateEvent
    // Fired when an item is inserted into the anvil
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onAnvilUpdate(net.minecraftforge.event.AnvilUpdateEvent event)  {
        clearRepairCost(event.getLeft());
        clearRepairCost(event.getRight());
    }

    // AnvilRepairEvent
    // Fired when output item is taken from anvil
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onAnvilUpdate(net.minecraftforge.event.entity.player.AnvilRepairEvent event)  {
        clearRepairCost(event.getItemResult());
    }

    static void clearRepairCost(ItemStack stack) {
        if (stack != null && stack.hasTagCompound()) {
            stack.getTagCompound().removeTag("RepairCost");
        }
    }
}
