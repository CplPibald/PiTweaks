package us.bemrose.mc.pitweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileEntityFastFurnace extends TileEntityFurnace {

    // Normal furnace takes 10s per item.  This cooks 20x faster which means 2 items per second
    static final int MULTIPLIER = 20;

    @Override
    public int getCookTime(ItemStack stack) {
        return 200 / MULTIPLIER;
    }

    // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
    public void update()
    {
        // FRAGILE: This can break if MC gets recompiled...
        final int furnaceBurnTime_Field = 4;

        if (this.isBurning())
        {
            try {
                // Mojang has to make things difficult
                // by declaring VERY IMPORTANT FIELDS as private!
                java.lang.reflect.Field[] fs = getClass().getSuperclass().getDeclaredFields();
                fs[furnaceBurnTime_Field].setAccessible(true);
                fs[furnaceBurnTime_Field].setInt(this, fs[furnaceBurnTime_Field].getInt(this) - MULTIPLIER + 1);
            } catch(IllegalAccessException e) {
                // We'll never get this exception because we set the accessibility
                // but Java's checked exceptions want me to type this
                // boilerplate anyway, because I'm just the programmer,
                // what the hell do I know?
                System.out.println("This didn't happen.");
            }
        }
        super.update();
    }

}