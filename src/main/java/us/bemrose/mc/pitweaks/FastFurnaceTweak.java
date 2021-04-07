package us.bemrose.mc.pitweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.BlastFurnaceTileEntity;
import net.minecraft.tileentity.SmokerTileEntity;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.nbt.CompoundNBT;

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

    // public static final TileEntityType<TileEntityFastFurnace> FASTFURNACE = register("furnace", TileEntityType.Builder.create(FurnaceTileEntity::new, Blocks.FURNACE));

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void onTETRegistration(final net.minecraftforge.event.RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.of(TileEntityFastFurnace::new, Blocks.FURNACE).build(null).setRegistryName("pitweaks:fastfurnace"));
        event.getRegistry().register(TileEntityType.Builder.of(TileEntityFastBlastFurnace::new, Blocks.BLAST_FURNACE).build(null).setRegistryName("pitweaks:fastblastfurnace"));
        event.getRegistry().register(TileEntityType.Builder.of(TileEntityFastSmoker::new, Blocks.SMOKER).build(null).setRegistryName("pitweaks:fastsmoker"));
        event.getRegistry().register(TileEntityType.Builder.of(TileEntityFastCampfire::new, Blocks.CAMPFIRE).build(null).setRegistryName("pitweaks:fastcampfire"));
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onBlockClicked(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {

        net.minecraft.util.math.BlockPos pos = event.getPos();
        net.minecraft.world.World world = event.getWorld();
        net.minecraft.block.Block block = world.getBlockState(pos).getBlock();

        // When a furnace is placed, replace its TE with our own
        if (TweakConfig.furnaceEnabled.get()) {
            if (block == Blocks.FURNACE) {
                net.minecraft.tileentity.TileEntity te = world.getBlockEntity(pos);
                if (te.getClass() == FurnaceTileEntity.class ) {
                    world.setBlockEntity(event.getPos(), new TileEntityFastFurnace((FurnaceTileEntity)te));
                }
            }
        } 
        if (TweakConfig.blastFurnaceEnabled.get()) {
            if (block == Blocks.BLAST_FURNACE) {
                net.minecraft.tileentity.TileEntity te = world.getBlockEntity(pos);
                if (te.getClass() == BlastFurnaceTileEntity.class) {
                    world.setBlockEntity(event.getPos(), new TileEntityFastBlastFurnace((BlastFurnaceTileEntity)te));
                }
            }
        }
        if (TweakConfig.smokerEnabled.get()) {
            if (block == Blocks.SMOKER) {
                net.minecraft.tileentity.TileEntity te = world.getBlockEntity(pos);
                if (te.getClass() == SmokerTileEntity.class) {
                    world.setBlockEntity(event.getPos(), new TileEntityFastSmoker((SmokerTileEntity)te));
                }
            }
        }
        if (TweakConfig.campfireEnabled.get()) {
            if (block == Blocks.CAMPFIRE) {
                net.minecraft.tileentity.TileEntity te = world.getBlockEntity(pos);
                if (te.getClass() == CampfireTileEntity.class) {
                    world.setBlockEntity(event.getPos(), new TileEntityFastCampfire((CampfireTileEntity)te));
                }
            }
        }
        if (TweakConfig.brewingEnabled.get()) {
            if (block == Blocks.BREWING_STAND) {
                net.minecraft.tileentity.TileEntity te = world.getBlockEntity(pos);
                if (te.getClass() == BrewingStandTileEntity.class) {
                    world.setBlockEntity(event.getPos(), new TileEntityFastBrewingStand((BrewingStandTileEntity)te));
                }
            }
        }
    }

    // @=====
    // Furnace
    //
    public static class TileEntityFastFurnace extends FurnaceTileEntity {

        public TileEntityFastFurnace() {};
        public TileEntityFastFurnace(FurnaceTileEntity te) {
            load(null, te.save(new CompoundNBT()));
        }

        @Override
        protected int getTotalCookTime() {
            return super.getTotalCookTime() / TweakConfig.furnaceMultiplier.get();
        }

        // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
        @Override
        public void tick() {
            int burnTime = dataAccess.get(0);

            if (burnTime > 0) {
                dataAccess.set(0, burnTime - TweakConfig.furnaceMultiplier.get() + 1);
            }
            super.tick();
        }
    }

    // @=====
    // Blast Furnace
    //
    public static class TileEntityFastBlastFurnace extends BlastFurnaceTileEntity {

        public TileEntityFastBlastFurnace() {};
        public TileEntityFastBlastFurnace(BlastFurnaceTileEntity te) {
            load(null, te.save(new CompoundNBT()));
        }

        @Override
        protected int getTotalCookTime() {
            return super.getTotalCookTime() / TweakConfig.furnaceMultiplier.get();
        }

        // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
        @Override
        public void tick() {
            int burnTime = dataAccess.get(0);

            if (burnTime > 0) {
                dataAccess.set(0, burnTime - TweakConfig.furnaceMultiplier.get() + 1);
            }
            super.tick();
        }
    }

    // @=====
    // Smoker
    //
    public static class TileEntityFastSmoker extends SmokerTileEntity {

        public TileEntityFastSmoker() {};
        public TileEntityFastSmoker(SmokerTileEntity te) {
            load(null, te.save(new CompoundNBT()));
        }

        @Override
        protected int getTotalCookTime() {
            return super.getTotalCookTime() / TweakConfig.furnaceMultiplier.get();
        }

        // Decrement burn time by multiplier ticks (minus one because TileEntityFurnace.update() decrements one)
        @Override
        public void tick() {
            int burnTime = dataAccess.get(0);

            if (burnTime > 0) {
                dataAccess.set(0, burnTime - TweakConfig.furnaceMultiplier.get() + 1);
            }
            super.tick();
        }
    }
    // @=====
    // Campfire
    //
    public static class TileEntityFastCampfire extends CampfireTileEntity {

        public TileEntityFastCampfire() {};
        public TileEntityFastCampfire(CampfireTileEntity te) {
            load(null, te.save(new CompoundNBT()));
        }

        // Add four extra ticks to cooking time, to make campfires hardcoded at 5x speed (items take 6 seconds)
        @Override
        public void tick() {
            if (!this.level.isClientSide && this.getBlockState().getValue(net.minecraft.block.CampfireBlock.LIT)) {
                // help the cook times along a bit
                net.minecraft.util.NonNullList<ItemStack> inv = this.getItems();
                for(int i = 0; i < inv.size(); ++i) {
                    ItemStack itemstack = inv.get(i);
                    if (!itemstack.isEmpty()) {
                        this.cookingProgress[i] += 4;
                    }
                }
            }
            super.tick();
        }
    }
    // @=====
    // Brewing Stand
    //
    public class TileEntityFastBrewingStand extends BrewingStandTileEntity {

        public TileEntityFastBrewingStand() {};
        public TileEntityFastBrewingStand(BrewingStandTileEntity te) {
            load(null, te.save(new CompoundNBT()));
        }

        @Override
        public void tick()
        {
            int brewTicks = TweakConfig.brewingTicks.get();
            final int brewTime_Field = 0;
            if (this.dataAccess.get(brewTime_Field) > brewTicks) {
                this.dataAccess.set(brewTime_Field, brewTicks);
            }
            super.tick();
        }
    }
}
