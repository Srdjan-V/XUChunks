package io.github.srdjanv.xuchunks.handelers;

import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import com.rwtema.extrautils2.power.PowerManager;
import io.github.srdjanv.xuchunks.Util;
import io.github.srdjanv.xuchunks.mixinaccessors.FTBUtilitiesTeamDataMixinAccessor;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TeamPowerManager {
    private static final int refreshRate = 600;
    private static int counter;
    private static Universe universe;

    //private static HashMap<TeamPower> teamPowers = new HashMap();

    @SubscribeEvent
    public static void universeLoaded(UniverseLoadedEvent.Finished event) {
        universe = event.getUniverse();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void updateTeamPowerData(TickEvent.ServerTickEvent event) {
        if (++counter % refreshRate == 0) {
            for (ForgeTeam team : universe.getTeams()) {
                int freq = Util.getFreq(team.getMembers());
                PowerManager.PowerFreq powerFreq = PowerManager.instance.getPowerFreq(freq);
                var teamPow = new TeamPower(team);
               teamPow.updateTeamData();
               teamPow.calculateMaxClaimChunks(powerFreq);
               teamPow.calculateMaxChunkloaderChunks(powerFreq);
            }
        }
    }


    public static class TeamPower {
        private FTBUtilitiesTeamData data = null;
        private final ForgeTeam team;

        public TeamPower(ForgeTeam team) {
            this.team = team;
        }

        public void updateTeamData() {
            data = FTBUtilitiesTeamData.get(team);
        }


        private void calculateMaxChunkloaderChunks(PowerManager.PowerFreq powerFreq) {
            ((FTBUtilitiesTeamDataMixinAccessor) data).xUChunks$setMaxChunkloaderChunksGrid((int) powerFreq.getPowerCreated());
        }

        private void calculateMaxClaimChunks(PowerManager.PowerFreq powerFreq) {
            ((FTBUtilitiesTeamDataMixinAccessor) data).xUChunks$setMaxClaimChunks((int) powerFreq.getPowerCreated());
        }

    }

}
