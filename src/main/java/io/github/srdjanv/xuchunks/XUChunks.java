package io.github.srdjanv.xuchunks;

import io.github.srdjanv.xuchunks.handelers.ChunkEventHandlers;
import io.github.srdjanv.xuchunks.handelers.TeamEventHandlers;
import io.github.srdjanv.xuchunks.handelers.TeamPowerManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import static io.github.srdjanv.xuchunks.XUChunks.MODID;
import static io.github.srdjanv.xuchunks.XUChunks.VERSION;

@Mod(modid = MODID, version = VERSION,
dependencies = "required:extrautils2;required:ftblib;required:ftbutilities")
public class XUChunks {
    public static final String MODID = Tags.MOD_ID;
    public static final String VERSION = Tags.VERSION;

    public XUChunks() {
        MinecraftForge.EVENT_BUS.register(TeamEventHandlers.class);
        MinecraftForge.EVENT_BUS.register(ChunkEventHandlers.class);
        MinecraftForge.EVENT_BUS.register(TeamPowerManager.class);
    }

    @EventHandler
    public static void serverStopped(FMLServerStoppedEvent event) {
        TeamPowerManager.reset();
    }
}
