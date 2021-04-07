package us.bemrose.mc.pitweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;

public class RepairCostTweak extends Tweak {

    @Override
    public void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        // Register for anvil events
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    // AnvilUpdateEvent
    // Fired when an item is inserted into the anvil
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onAnvilUpdate(net.minecraftforge.event.AnvilUpdateEvent event)  {

        ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

        if (TweakConfig.anvilNoRepairCost.get()) {
            clearRepairCost(left);
            clearRepairCost(right);
        }

        // Override book handling
        // if anvilAlwaysAllowBooks, then any book can be applied to any item
        // if anvilOverlevelBooks, then books can be applied for ever higher enchantments
        // if neither, revert to default book handling
        
        if (left.isEmpty() || right.isEmpty()) { return; }
        if (!(TweakConfig.anvilAlwaysAllowBooks.get() || TweakConfig.anvilOverlevelBooks.get())) { return; }

		if(net.minecraft.item.Items.ENCHANTED_BOOK == right.getItem()) {

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

                    if (!TweakConfig.anvilOverlevelBooks.get()) {
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
                if(name != null && !name.isEmpty() && name != out.getDisplayName().getString()){
                    out.setHoverName(new net.minecraft.util.text.StringTextComponent(name));
                    cost++;
                }
                event.setOutput(out);
                event.setCost(cost);
            }
            else {
                event.setCanceled(true);
            }
        }

        // Disenchant item, if right side is an empty book
        else if(TweakConfig.anvilDisenchant.get() && net.minecraft.item.Items.BOOK == right.getItem()) {

            net.minecraft.nbt.ListNBT enchTags = left.getEnchantmentTags();
            if (!enchTags.isEmpty()) {

                ItemStack out = left.copy();
                net.minecraft.nbt.ListNBT newTags = enchTags.copy();
                newTags.remove(0);
                out.addTagElement("Enchantments", newTags);

                net.minecraft.nbt.CompoundNBT ench = enchTags.getCompound(0);
                int cost = getApplyCost(getEnchantmentFromStringId(ench.getString("id")), 1);

                event.setOutput(out);
                event.setCost(cost);
                event.setMaterialCost(1);
            }
        }
    }

    // AnvilRepairEvent
    // Fired when output item is taken from anvil
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onAnvilUpdate(net.minecraftforge.event.entity.player.AnvilRepairEvent event)  {
        if (TweakConfig.anvilNoRepairCost.get()) {
            clearRepairCost(event.getItemResult());
        }
        event.setBreakChance(TweakConfig.anvilBreakChance.get().floatValue());

        if(TweakConfig.anvilDisenchant.get() && net.minecraft.item.Items.BOOK == event.getIngredientInput().getItem()) {
            ItemStack left = event.getItemInput();
            net.minecraft.nbt.ListNBT enchTags = left.getEnchantmentTags();
            if (!enchTags.isEmpty()) {
                net.minecraft.nbt.CompoundNBT ench = enchTags.getCompound(0);

                ItemStack book = new ItemStack(net.minecraft.item.Items.ENCHANTED_BOOK);
                net.minecraft.nbt.ListNBT newTags = new net.minecraft.nbt.ListNBT();
                newTags.add(ench);
                book.getOrCreateTag().put("StoredEnchantments", newTags);

                net.minecraft.entity.player.PlayerEntity player = event.getPlayer();
                net.minecraft.inventory.InventoryHelper.dropItemStack(player.level, player.getX(), player.getY(), player.getZ(), book);
            }
        }
    }

    static private boolean canApplyEnchant(ItemStack i, Enchantment e) {
        if (TweakConfig.anvilAlwaysAllowBooks.get()) { return true; }
        if (net.minecraft.item.Items.ENCHANTED_BOOK == i.getItem()) { return true; }
        if (!e.canEnchant(i)) { return false; }
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

    static private Enchantment getEnchantmentFromStringId(String id) {
        return net.minecraft.util.registry.Registry.ENCHANTMENT.get(net.minecraft.util.ResourceLocation.tryParse(id));
    }
    
    static void clearRepairCost(ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTag()) {
            stack.removeTagKey("RepairCost");
        }
    }
}
