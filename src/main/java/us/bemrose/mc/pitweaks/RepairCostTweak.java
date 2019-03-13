package us.bemrose.mc.pitweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;

public class RepairCostTweak extends Tweak {

    public static boolean noRepairCost = true;
    public static boolean allowOverlevelBooks = true;
    public static boolean alwaysAllowBooks = true;

    public RepairCostTweak(boolean nrc, boolean aob, boolean aab) {
        noRepairCost = nrc;
        allowOverlevelBooks = aob;
        alwaysAllowBooks = aab;
    }

    @Override
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        // Register for anvil events
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    // AnvilUpdateEvent
    // Fired when an item is inserted into the anvil
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onAnvilUpdate(net.minecraftforge.event.AnvilUpdateEvent event)  {

        ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

        if (noRepairCost) {
            clearRepairCost(left);
            clearRepairCost(right);
        }

        // Override book handling
        // if alwaysAllowBooks, then any book can be applied to any item
        // if allowOverlevelBooks, then books can be applied for ever higher enchantments
        // if neither, revert to default book handling
        // Some code borrowed and adapted from Vazkii/Quark
        
        if (!(alwaysAllowBooks || allowOverlevelBooks)) { return; }

		if(!left.isEmpty() && !right.isEmpty() && net.minecraft.init.Items.ENCHANTED_BOOK == right.getItem()) {

            int cost = 0;
            java.util.Map<Enchantment, Integer> currentEnchants = EnchantmentHelper.getEnchantments(left);

            for (java.util.Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(right).entrySet()) {

                Enchantment ench = entry.getKey();
                int level = entry.getValue();
                if(null == ench || 0 == level) { continue; }
                
                if (canApplyEnchant(left, ench)) {
                    int currentLevel = 0;
                    if (currentEnchants.containsKey(ench)) {
                        currentLevel = currentEnchants.get(ench);
                    }

                    if (currentLevel > level) { continue; }

                    int outLevel = (currentLevel == level) ? level + 1 : level;

                    if (!allowOverlevelBooks) {
                        outLevel = Math.min(outLevel, ench.getMaxLevel());
                    }

                    if (outLevel > currentLevel) {
                        currentEnchants.put(ench, outLevel);
                        cost += getApplyCost(ench, outLevel);
                    }
                }
            }

            if (cost > 0) {
                ItemStack out = left.copy();
                EnchantmentHelper.setEnchantments(currentEnchants, out);
                String name = event.getName();
                if(name != null && !name.isEmpty()){
                    out.setStackDisplayName(name);
                    cost++;
                }
                event.setOutput(out);
                event.setCost(cost);
            }
            else {
                event.setCanceled(true);
            }
        }
    }

    static private boolean canApplyEnchant(ItemStack i, Enchantment e) {
        if (alwaysAllowBooks) { return true; }
        if (net.minecraft.init.Items.ENCHANTED_BOOK == i.getItem()) { return true; }
        if (!e.canApply(i)) { return false; }
        for (Enchantment enchCompare : EnchantmentHelper.getEnchantments(i).keySet()) {
            if (enchCompare != null && enchCompare != e && !enchCompare.isCompatibleWith(e)) {
                return false;
            }
        }
        return true;
    }

    static private int getApplyCost(Enchantment e, int lvl) {
        
        int rarityFactor =  10; // 10 for unknown rarity - should NEVER happen
                            
        Enchantment.Rarity r = e.getRarity();
        if (Enchantment.Rarity.COMMON == r)         { rarityFactor = 1; }
        else if(Enchantment.Rarity.UNCOMMON == r)   { rarityFactor = 2; }
        else if(Enchantment.Rarity.RARE == r)       { rarityFactor = 4; }
        else if(Enchantment.Rarity.VERY_RARE == r)  { rarityFactor = 8; }
        
        return rarityFactor * lvl;
    }
    
    // AnvilRepairEvent
    // Fired when output item is taken from anvil
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onAnvilUpdate(net.minecraftforge.event.entity.player.AnvilRepairEvent event)  {
        if (noRepairCost) {
            clearRepairCost(event.getItemResult());
        }
    }

    static void clearRepairCost(ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTagCompound()) {
            stack.getTagCompound().removeTag("RepairCost");
        }
    }
}
