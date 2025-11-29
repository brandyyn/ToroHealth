package net.torocraft.torohealthmod;

import java.util.logging.Logger;

import net.torocraft.torohealthmod.client.configuration.ConfigurationHandler;
import net.torocraft.torohealthmod.net.DamageMessage;
import net.torocraft.torohealthmod.server.DamageSyncEventHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(
        modid = ToroHealthMod.MODID,
        name = Tags.MODNAME,
        version = Tags.VERSION,
        guiFactory = ToroHealthMod.GUI_FACTORY_CLASS)
public class ToroHealthMod {

    public static final String MODID = "torohealthmod";

    public static final String GUI_FACTORY_CLASS =
            "net.torocraft.torohealthmod.client.configuration.gui.GuiFactory";

    public static final SimpleNetworkWrapper NETWORK =
            NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Client-side config handler
        if (event.getSide() == Side.CLIENT) {
            FMLCommonHandler.instance()
                    .bus()
                    .register(new ConfigurationHandler(event.getSuggestedConfigurationFile()));
        }

        // Register full-damage packet handler (server -> client)
        NETWORK.registerMessage(DamageMessage.Handler.class, DamageMessage.class, 0, Side.CLIENT);

        // Server-side sync of full damage
        MinecraftForge.EVENT_BUS.register(new DamageSyncEventHandler());

        Logger.getLogger(Tags.MODNAME)
                .info(Tags.MODNAME + " initialised with full damage sync enabled.");
    }

}
