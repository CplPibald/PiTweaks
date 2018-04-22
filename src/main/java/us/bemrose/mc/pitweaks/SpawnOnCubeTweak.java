package us.bemrose.mc.pitweaks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;

public class SpawnOnCubeTweak extends Tweak {

    public SpawnOnCubeTweak() {
    }

    @Override
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }
    
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onSpawnCheck(net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn event) {

        BlockPos pos = event.getEntity().getPosition().down();
        IBlockState state = event.getWorld().getBlockState(pos);

        // Cancel the spawn if the block below has a collision box, but is not a full cube
        if(!(state.isFullCube() || state.getCollisionBoundingBox(event.getWorld(), pos) == Block.NULL_AABB)) {
            event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
        }
    }
}
