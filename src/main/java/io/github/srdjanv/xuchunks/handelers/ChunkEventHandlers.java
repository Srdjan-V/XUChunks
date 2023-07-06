package io.github.srdjanv.xuchunks.handelers;

import com.feed_the_beast.ftbutilities.events.chunks.ChunkModifiedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChunkEventHandlers {

    @SubscribeEvent
    public static void chunkClaimed(ChunkModifiedEvent.Claimed event) {
       // refreshPlayersInTeam(event.getTeam());
    }

}
