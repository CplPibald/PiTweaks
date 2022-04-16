package us.bemrose.mc.pitweaks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

public class SpawnOnCubeTweak extends Tweak {

    @Override
    public void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {

        // For reload support, always register here and check on event fire
        if (TweakConfig.spawnFullCubeEnabled.get() || TweakConfig.noInvulnerabilityTicks.get() || !TweakConfig.allowPhantoms.get()) { 
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
        }
    }
    
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onSpawnCheck(net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn event) {

        if (event.isSpawner()) { return; }

        boolean doSpawn = true;
        if (TweakConfig.spawnFullCubeEnabled.get()) {
            BlockPos pos = event.getEntity().blockPosition().below();
            BlockState state = event.getWorld().getBlockState(pos);

            boolean isFullCube = state.isCollisionShapeFullBlock(event.getWorld(), pos);
            // block spawn unless block underneath is:
            //   full cube
            //   air
            //   waterlogged
            if (!isFullCube && !state.isAir() && state.getFluidState().isEmpty()) {
                doSpawn = false;
            }
        }

        if (!TweakConfig.allowPhantoms.get() && event.getEntity().getType() == net.minecraft.entity.EntityType.PHANTOM) {
            doSpawn = false;
        }

        if(!doSpawn) {
            event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
        }
    }

    // Other entity tweaks below.  This file should probably be renamed

    // Invulnerability tweak
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onLivingHurt(net.minecraftforge.event.entity.living.LivingHurtEvent event) {
        if (TweakConfig.noInvulnerabilityTicks.get()) {
            event.getEntityLiving().invulnerableTime = 0;
        }
    }

    // End Elytra flight tweak
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
        if (event.player.isFallFlying() && event.player.isShiftKeyDown()) {
            if (TweakConfig.crouchStopsFlight.get()) {
                event.player.stopFallFlying();
            }
        }
    }
}
