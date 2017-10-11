package us.bemrose.mc.pitweaks;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityBoat;

public class BuoyantBoatTweak extends Tweak {

    public static boolean dontEjectPassengers = false;
    
    public BuoyantBoatTweak(boolean dep) {
        // Code smell: setting static member from constructor.
        // But hey, this is a singleton.
        dontEjectPassengers = dep;
    }

    @Override
    public void preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent event) {
        // Register for events
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    // Register new entity
    @SubscribeEvent
    public static void registerEntities(final net.minecraftforge.event.RegistryEvent.Register<net.minecraftforge.fml.common.registry.EntityEntry> event) {
        event.getRegistry().register(new net.minecraftforge.fml.common.registry.EntityEntry(EntityBuoyantBoat.class, "buoyant_boat"));
    }
    
    // EntityJoinEvent
    @SubscribeEvent
    public void onEntityJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {

        // When a boat is placed, replace its entity with our own
        if (!event.getWorld().isRemote && event.getEntity().getClass() == EntityBoat.class) {
            event.getWorld().spawnEntity(new EntityBuoyantBoat((EntityBoat)event.getEntity()));
            event.getEntity().setDead();
        }
    }
}
