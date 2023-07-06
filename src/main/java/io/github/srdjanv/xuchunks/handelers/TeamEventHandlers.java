package io.github.srdjanv.xuchunks.handelers;

import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamChangedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamConfigEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import com.rwtema.extrautils2.power.PowerManager;
import gnu.trove.set.hash.TIntHashSet;
import io.github.srdjanv.xuchunks.Util;
import io.github.srdjanv.xuchunks.XUChunks;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TeamEventHandlers {

    @SubscribeEvent
    public static void playerLogin(ForgePlayerLoggedInEvent event) {
        refreshPlayersInTeam(event.getTeam());
    }

    @SubscribeEvent
    public static void teamPlayerJoined(ForgeTeamPlayerJoinedEvent event) {
        refreshPlayersInTeam(event.getTeam());
    }

    @SubscribeEvent
    public static void teamPlayerLeft(ForgeTeamPlayerLeftEvent event) {
        refreshPlayersInTeam(event.getTeam(), event.getPlayer());
    }

    @SubscribeEvent
    public static void teamChanged(ForgeTeamChangedEvent event) {
        refreshPlayersInTeam(event.getTeam());
    }

    @SubscribeEvent
    public static void teamConfig(ForgeTeamConfigEvent event) {
        final ForgeTeam forgeTeam = event.getTeam();
        final ConfigGroup configGroup = event.getConfig();
        final ConfigGroup modConfigGroup = configGroup.getGroup(XUChunks.MODID);

        modConfigGroup.addBool(
                "Secure Inventories",
                () -> {
                    IntArrayList freqList = Util.getFreqOfPlayers(forgeTeam.getMembers());
                    if (freqList == null) return false;

                    for (int integer : freqList) {
                        if (PowerManager.instance.lockedFrequencies.contains(integer)) {
                            PowerManager.instance.lockedFrequencies.addAll(freqList);
                            return true;
                        }
                    }
                    return false;
                },
                newVal -> {
                    IntArrayList freqList = Util.getFreqOfPlayers(forgeTeam.getMembers());
                    if (freqList == null) return;

                    if (newVal) {
                        PowerManager.instance.lockedFrequencies.addAll(freqList);
                    } else {
                        PowerManager.instance.lockedFrequencies.removeAll(freqList);
                    }
                },
                false);

    }

    private static void refreshPlayersInTeam(ForgeTeam team, ForgePlayer removedPlayer) {
        List<ForgePlayer> currentPlayers = new ObjectArrayList<>(team.getMembers());
        currentPlayers.remove(removedPlayer);
        refreshPlayersInTeam(currentPlayers);
    }

    private static void refreshPlayersInTeam(ForgeTeam team) {
        refreshPlayersInTeam(team.getMembers());
        FTBUtilitiesTeamData.get(team).getMaxChunkloaderChunks();
    }

    private static void refreshPlayersInTeam(List<ForgePlayer> forgePlayers) {
        IntArrayList freqList = Util.getFreqOfPlayers(forgePlayers);
        if (freqList == null) return;

        for (final int freq : freqList) {
            TIntHashSet players = PowerManager.instance.alliances.get(freq);
            if (players == null) {
                players = new TIntHashSet();
                PowerManager.instance.alliances.put(freq, players);
            } else {
                players.clear();
            }

            players.addAll(freqList.stream().filter(integer -> integer != freq).mapToInt(Integer::intValue).toArray());
        }

        PowerManager.instance.reassignValues();
    }
}
