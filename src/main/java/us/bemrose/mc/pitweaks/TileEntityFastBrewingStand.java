package us.bemrose.mc.pitweaks;

import net.minecraft.tileentity.TileEntityBrewingStand;

public class TileEntityFastBrewingStand extends TileEntityBrewingStand {

    // If stand is brewing (brewTime > 0), set the remaining time to 1 so that it will complete next tick.
    @Override
    public void update()
    {
        final int brewTime_Field = 0;
        if (this.getField(brewTime_Field) > TweakConfig.brewing.brewTicks) {
            this.setField(brewTime_Field, TweakConfig.brewing.brewTicks);
        }
        super.update();
    }

    // For some reason, Forge decided that modded tile entities should be nuked every time their 
    // block changes state. Overriding shouldRefresh() to dial it back to vanilla behavior so it
    // doesn't dump data every time someone changes the model by inserting a bottle.
    @Override
    public boolean shouldRefresh(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, net.minecraft.block.state.IBlockState oldState, net.minecraft.block.state.IBlockState newState) {
        return newState.getBlock() != net.minecraft.init.Blocks.BREWING_STAND;
    }
}