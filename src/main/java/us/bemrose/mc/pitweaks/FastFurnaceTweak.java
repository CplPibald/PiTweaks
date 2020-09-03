package us.bemrose.mc.pitweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;

public class FastFurnaceTweak extends Tweak {

    @Override
    public void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        // Register block place event
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void register(net.minecraftforge.eventbus.api.IEventBus bus) {
        // Register new tile entities
        bus.register(TileEntityFastFurnace.class);
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void onTETRegistration(final net.minecraftforge.event.RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(TileEntityFastFurnace::new, Blocks.FURNACE).build(null).setRegistryName("pitweaks:fastfurnace"));
        event.getRegistry().register(TileEntityType.Builder.create(TileEntityFastBlastFurnace::new, Blocks.BLAST_FURNACE).build(null).setRegistryName("pitweaks:fastblastfurnace"));
        event.getRegistry().register(TileEntityType.Builder.create(TileEntityFastSmoker::new, Blocks.SMOKER).build(null).setRegistryName("pitweaks:fastsmoker"));
        event.getRegistry().register(TileEntityType.Builder.create(TileEntityFastCampfire::new, Blocks.CAMPFIRE).build(null).setRegistryName("pitweaks:fastcampfire"));
    }

    // BlockEvent
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onBlockPlaced(net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent event) {

        // When a furnace is placed, replace its tile entity with our own
        if (TweakConfig.furnaceEnabled.get()) {
            if (event.getPlacedBlock().getBlock() == Blocks.FURNACE) {
                event.getWorld().getWorld().setTileEntity(event.getBlockSnapshot().getPos(), new TileEntityFastFurnace());
            }
        }
        if (TweakConfig.blastFurnaceEnabled.get()) {
            if (event.getPlacedBlock().getBlock() == Blocks.BLAST_FURNACE) {
                event.getWorld().getWorld().setTileEntity(event.getBlockSnapshot().getPos(), new TileEntityFastBlastFurnace());
            }
        }
        if (TweakConfig.smokerEnabled.get()) {
            if (event.getPlacedBlock().getBlock() == Blocks.SMOKER) {
                event.getWorld().getWorld().setTileEntity(event.getBlockSnapshot().getPos(), new TileEntityFastSmoker());
            }
        }
        if (TweakConfig.campfireEnabled.get()) {
            if (event.getPlacedBlock().getBlock() == Blocks.CAMPFIRE) {
                event.getWorld().getWorld().setTileEntity(event.getBlockSnapshot().getPos(), new TileEntityFastCampfire());
            }
        }
        if (TweakConfig.brewingEnabled.get()) {
            if (event.getPlacedBlock().getBlock() == Blocks.BREWING_STAND) {
                event.getWorld().getWorld().setTileEntity(event.getBlockSnapshot().getPos(), new TileEntityFastBrewingStand());
            }
        }
    }

    // @=====
    // Furnace
    //
    public static class TileEntityFastFurnace extends net.minecraft.tileentity.FurnaceTileEntity {

        @Override
        protected int getCookTime() {
            return super.getCookTime() / TweakConfig.furnaceMultiplier.get();
        }

        // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
        @Override
        public void tick() {
            int burnTime = furnaceData.get(0);

            if (burnTime > 0) {
                furnaceData.set(0, burnTime - TweakConfig.furnaceMultiplier.get() + 1);
            }
            super.tick();
        }
    }

    // @=====
    // Blast Furnace
    //
    public static class TileEntityFastBlastFurnace extends net.minecraft.tileentity.BlastFurnaceTileEntity {

        @Override
        protected int getCookTime() {
            return super.getCookTime() / TweakConfig.furnaceMultiplier.get();
        }

        // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
        @Override
        public void tick() {
            int burnTime = furnaceData.get(0);

            if (burnTime > 0) {
                furnaceData.set(0, burnTime - TweakConfig.furnaceMultiplier.get() + 1);
            }
            super.tick();
        }
    }

    // @=====
    // Smoker
    //
    public static class TileEntityFastSmoker extends net.minecraft.tileentity.SmokerTileEntity {

        @Override
        protected int getCookTime() {
            return super.getCookTime() / TweakConfig.furnaceMultiplier.get();
        }

        // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
        @Override
        public void tick() {
            int burnTime = furnaceData.get(0);

            if (burnTime > 0) {
                furnaceData.set(0, burnTime - TweakConfig.furnaceMultiplier.get() + 1);
            }
            super.tick();
        }
    }
    // @=====
    // Campfire
    //
    public static class TileEntityFastCampfire extends net.minecraft.tileentity.CampfireTileEntity {

        // Add four extra ticks to cookingTimes, to make campfires hardcoded at 5x speed (items take 6 seconds)
        @Override
        public void tick() {
            if (!this.world.isRemote && this.getBlockState().get(net.minecraft.block.CampfireBlock.LIT)) {
                // help the cook times along a bit
                for(int i = 0; i < this.inventory.size(); ++i) {
                    ItemStack itemstack = this.inventory.get(i);
                    if (!itemstack.isEmpty()) {
                        this.cookingTimes[i] += 4;
                    }
                }
            }
            super.tick();
        }
    }
    // @=====
    // Brewing Stand
    //
    public class TileEntityFastBrewingStand extends net.minecraft.tileentity.BrewingStandTileEntity {

        @Override
        public void tick()
        {
            int brewTicks = TweakConfig.brewingTicks.get();
            final int brewTime_Field = 0;
            if (this.field_213954_a.get(brewTime_Field) > brewTicks) {
                this.field_213954_a.set(brewTime_Field, brewTicks);
            }
            super.tick();
        }
    }
}
