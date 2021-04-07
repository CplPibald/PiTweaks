package us.bemrose.mc.pitweaks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.world.server.ServerWorld;

public class ForestTweak extends Tweak {

    final int MAX_LEAVES_PER_TICK = 20;

    @Override
    public void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {

        // For reload support, always register here and check on event fire
        if (TweakConfig.instantLeafDecay.get()) { 
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
        }
    }

    // Algorithm credit to https://github.com/lumien231/QuickLeafDecay

    java.util.LinkedList<LeafData> decayQueue = new java.util.LinkedList<LeafData>();
    
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onNotify(net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent event) {
        // PiTweaks.LOGGER.debug("onNotify");
        BlockPos eventPos = event.getPos();
        net.minecraft.world.IWorld w = event.getWorld();
        for (net.minecraft.util.Direction dir : event.getNotifiedSides()) {
            BlockPos p = eventPos.relative(dir);
            BlockState s = w.getBlockState(p);
            Block b = s.getBlock();
            if (b instanceof LeavesBlock) {
                // force a random tick for the leaves block, to tell it to decay now
                decayQueue.addLast(new LeafData((ServerWorld)w, p));
            }
        }
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void tick(net.minecraftforge.event.TickEvent.ServerTickEvent event) {
        int count = 0;
        while (count < MAX_LEAVES_PER_TICK && !decayQueue.isEmpty()) {
            LeafData ld = decayQueue.removeFirst();
            ServerWorld w = ld.world.get();
            BlockState s = w.getBlockState(ld.pos);
            java.util.Random r = w.getRandom();
            s.tick(w, ld.pos, r);
            s.randomTick(w, ld.pos, r);
            count++;
        }
    }

    class LeafData {
        public java.lang.ref.WeakReference<ServerWorld> world;
        public BlockPos pos;
        public LeafData(ServerWorld w, BlockPos p) {
            world = new java.lang.ref.WeakReference<ServerWorld>(w);
            pos = p;
        }
    }

}
