package io.github.srdjanv.xuchunks.handelers;

import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftbutilities.data.ClaimedChunk;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import com.rwtema.extrautils2.power.IPower;
import com.rwtema.extrautils2.power.IWorldPowerMultiplier;
import com.rwtema.extrautils2.power.PowerManager;
import io.github.srdjanv.xuchunks.Util;
import io.github.srdjanv.xuchunks.mixinaccessors.FTBUtilitiesTeamDataMixinAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.OptionalInt;

public class TeamPowerManager {
    private static Universe universe;
    private static final Map<ForgeTeam, TeamPower> teamPowers = new Object2ObjectOpenHashMap<>();

    public static void reset() {
        for (TeamPower value : teamPowers.values()) {
            PowerManager.instance.removePowerHandler(value.power);
        }
        teamPowers.clear();
    }

    @SubscribeEvent
    public static void universeLoaded(UniverseLoadedEvent.Finished event) {// TODO: 07/07/2023 fix initial power draw setting 
        universe = event.getUniverse();
        for (ForgeTeam team : event.getUniverse().getTeams()) {
            teamPowers.computeIfAbsent(team, TeamPower::new).tick();
        }
    }

    public static void tickTeamPower(EntityPlayer player) {
        if (universe != null) {
            var forgePlayer = universe.getPlayer(player);
            if (forgePlayer.hasTeam()) {
                tickTeamPower(forgePlayer.team);
            }
        }
    }

    public static void tickTeamPower(ForgeTeam team) {
        if (!ClaimedChunks.isActive()) {
            return;
        }
        teamPowers.computeIfAbsent(team, TeamPower::new).tick();
    }

    public static class TeamPower {
        private final FTBUtilitiesTeamData data;
        private final ForgeTeam team;
        private final DynamicPower power;
        private PowerManager.PowerFreq powerFreq;

        public TeamPower(ForgeTeam team) {
            this.team = team;
            this.data = FTBUtilitiesTeamData.get(team);
            this.power = new DynamicPower(team);
            Integer freq = Util.getFreq(team);
            if (freq != null)
                power.setFrequency(freq);

            PowerManager.instance.addPowerHandler(power);
        }

        private void tick() {
            Integer freq = Util.getFreq(team);
            if (freq == null) return;
            power.setFrequency(freq);
            powerFreq = PowerManager.instance.getPowerFreq(freq);

            calculateMaxClaimChunks();
            calculateMaxChunkloaderChunks();
            PowerManager.instance.reassignValues();
        }


        private void calculateMaxChunkloaderChunks() {
            ((FTBUtilitiesTeamDataMixinAccessor) data).xUChunks$setMaxChunkloaderChunksGrid(0);

            var loadedChunks = 0;
            var chunks = ClaimedChunks.instance.getTeamChunks(team, OptionalInt.empty());
            for (ClaimedChunk chunk : chunks) {
                if (chunk.isLoaded()) {
                    loadedChunks++;
                }
            }
            if (loadedChunks > data.getMaxChunkloaderChunks()) {
                power.setPower(loadedChunks - data.getMaxChunkloaderChunks());
            } else power.setPower(0);

            ((FTBUtilitiesTeamDataMixinAccessor) data).xUChunks$setMaxChunkloaderChunksGrid((int) powerFreq.getPowerCreated());
        }

        private void calculateMaxClaimChunks() {
            if (false) {
                ((FTBUtilitiesTeamDataMixinAccessor) data).xUChunks$setMaxClaimChunks((int) powerFreq.getPowerCreated());

                return;
            }

            ((FTBUtilitiesTeamDataMixinAccessor) data).xUChunks$setMaxClaimChunks((int) powerFreq.getPowerCreated());

        }
    }


    private static class DynamicPower implements IPower {
        private final ForgeTeam team;
        private int frequency;
        private int power;

        public DynamicPower(ForgeTeam team) {
            this.team = team;
        }

        public void setPower(int power) {
            this.power = power;
        }

        @Override
        public float getPower() {
            return power;
        }

        @Override
        public IWorldPowerMultiplier getMultiplier() {
            return IWorldPowerMultiplier.CONSTANT;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Override
        public int frequency() {
            return frequency;
        }

        @Override
        public void powerChanged(boolean b) {
        }

        @Nullable
        @Override
        public World world() {
            return null;
        }

        @Override
        public String getName() {
            return team.toString();
        }

        @Override
        public boolean isLoaded() {
            return true;
        }

        @Nullable
        @Override
        public BlockPos getLocation() {
            return null;
        }
    }
}
