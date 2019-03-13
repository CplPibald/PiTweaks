package us.bemrose.mc.pitweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileEntityFastFurnace extends TileEntityFurnace {

    @Override
    public int getCookTime(ItemStack stack) {
        return 200 / TweakConfig.furnace.multiplier;
    }

    // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
    public void update()
    {
        final int furnaceBurnTime_Field = 0;

        if (this.isBurning())
        {
            this.setField(furnaceBurnTime_Field, this.getField(furnaceBurnTime_Field) - TweakConfig.furnace.multiplier + 1);
        }
        super.update();
    }

}