package us.bemrose.mc.pitweaks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

public class SpawnOnCubeTweak extends Tweak {

    @Override
    public void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {

        // For reload support, always register here and check on event fire
        if (TweakConfig.spawnFullCubeEnabled.get()) { 
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
        }
    }
    
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onSpawnCheck(net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn event) {

        // Need to check this here to enable changing in-game...
        // but Forge 1.15 doesn't support reload anyway
        // if (!TweakConfig.spawnFullCubeEnabled.get()) { return; }
    
        BlockPos pos = event.getEntity().getPosition().down();
        BlockState state = event.getWorld().getBlockState(pos);

        boolean isFullCube = (state.getCollisionShape(event.getWorld(), pos) == net.minecraft.util.math.shapes.VoxelShapes.fullCube());

        // Allow the spawn if the block below has a collision box that is not a full cube.
        // always allow spawner spawns and spawns in water
        // Otherwise cancel.
        if(!(isFullCube || event.isSpawner()) && state.getFluidState().isEmpty()) {
            event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
        }
    }
}
