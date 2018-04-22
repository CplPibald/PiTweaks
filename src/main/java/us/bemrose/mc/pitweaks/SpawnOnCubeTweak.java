package us.bemrose.mc.pitweaks;

import net.minecraft.util.math.BlockPos;

public class SpawnOnCubeTweak extends Tweak {

    public SpawnOnCubeTweak() {
    }

    @Override
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        // Register block place event
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }
    
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
	public void onSpawnCheck(net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn event) {

		BlockPos pos = event.getEntity().getPosition();
        
		if(!event.getWorld().getBlockState(pos.down()).isFullCube()) {
			event.setResult(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
        }    
    }
}
