package io.github.srdjanv.xuchunks.mixin;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamData;
import com.feed_the_beast.ftbutilities.FTBUtilitiesPermissions;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import com.feed_the_beast.ftbutilities.data.FTBUtilitiesTeamData;
import io.github.srdjanv.xuchunks.mixinaccessors.FTBUtilitiesTeamDataMixinAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = FTBUtilitiesTeamData.class, remap = false)
public abstract class FTBUtilitiesTeamDataMixin extends TeamData implements FTBUtilitiesTeamDataMixinAccessor {
    public FTBUtilitiesTeamDataMixin(ForgeTeam t) {
        super(t);
        throw new AssertionError();
    }

    @Shadow
    private int cachedMaxClaimChunks;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getMaxClaimChunks() {
        if (!ClaimedChunks.isActive()) {
            return -1;
        } else if (!team.isValid()) {
            return -2;
        } else if (cachedMaxClaimChunks >= 0) {
            return cachedMaxClaimChunks + xUChunks$MaxClaimChunksGrid;
        }

        cachedMaxClaimChunks = 0;

        for (ForgePlayer player : team.getMembers()) {
            cachedMaxClaimChunks += player.getRankConfig(FTBUtilitiesPermissions.CLAIMS_MAX_CHUNKS).getInt();
        }

        return cachedMaxClaimChunks + xUChunks$MaxClaimChunksGrid;
    }

    @Unique
    private int xUChunks$MaxClaimChunksGrid;

    @Unique
    @Override
    public void xUChunks$setMaxClaimChunks(int chunks) {
        xUChunks$MaxClaimChunksGrid = chunks;
    }

    @Unique
    @Override
    public int xUChunks$getMaxClaimChunks() {
        return xUChunks$MaxClaimChunksGrid;
    }

    @Shadow
    private int cachedMaxChunkloaderChunks;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getMaxChunkloaderChunks() {
        if (!ClaimedChunks.isActive()) {
            return -1;
        } else if (!team.isValid()) {
            return -2;
        } else if (cachedMaxChunkloaderChunks >= 0) {
            return cachedMaxChunkloaderChunks + xUChunks$maxChunkloaderChunksGrid;
        }

        cachedMaxChunkloaderChunks = 0;

        for (ForgePlayer player : team.getMembers()) {
            cachedMaxChunkloaderChunks += player.getRankConfig(FTBUtilitiesPermissions.CHUNKLOADER_MAX_CHUNKS).getInt();
        }

        return cachedMaxChunkloaderChunks + xUChunks$maxChunkloaderChunksGrid;
    }

    @Unique
    private int xUChunks$maxChunkloaderChunksGrid;

    @Unique
    @Override
    public void xUChunks$setMaxChunkloaderChunksGrid(int chunks) {
        xUChunks$maxChunkloaderChunksGrid = chunks;
    }

    @Unique
    @Override
    public int xUChunks$getMaxChunkloaderChunksGrid() {
        return xUChunks$maxChunkloaderChunksGrid;
    }
}
