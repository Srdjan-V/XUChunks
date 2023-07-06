package io.github.srdjanv.xuchunks;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.mojang.authlib.GameProfile;
import com.rwtema.extrautils2.power.Freq;
import com.rwtema.extrautils2.power.PowerManager;
import com.rwtema.extrautils2.power.PowerSettings;
import com.rwtema.extrautils2.utils.helpers.NBTHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Util {
    private Util() {
    }

    private static final Random rand;

    static {
        try {
            var randF = Freq.class.getDeclaredField("rand");
            randF.setAccessible(true);
            rand = (Random) randF.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static IntArrayList getFreqOfPlayers(List<ForgePlayer> members) {
        IntArrayList freqList = null;
        for (ForgePlayer member : members) {
            if (freqList == null) freqList = new IntArrayList();
            freqList.add(getBasePlayerFreq(member));
        }
        return freqList;
    }

    public static int getFreq(List<ForgePlayer> players) {
        for (ForgePlayer player : players) {
            return getBasePlayerFreq(player);
        }
        throw new RuntimeException();
    }

    private static int getBasePlayerFreq(ForgePlayer player) {
        var playerEntity = player.getNullablePlayer();
        if (playerEntity != null) {
            return Freq.getBasePlayerFreq(playerEntity);
        }

        // TODO: 06/07/2023 fix, this will not work 
        NBTTagCompound playerNbt = player.getPlayerNBT();
        NBTTagCompound xu2Tag = NBTHelper.getOrInitTagCompound(playerNbt, "XU2");
        int i = xu2Tag.getInteger("Frequency");
        PowerSettings.instance.markDirty();
        GameProfile gameProfile = player.getProfile();
        if (i != 0) {
            PowerManager.instance.frequncies.putIfAbsent(i, gameProfile);
            return i;
        } else {
            synchronized (PowerManager.MUTEX) {
                UUID uuid = EntityPlayer.getUUID(gameProfile);
                rand.setSeed(uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits() ^ (long) ObjectUtils.hashCode(gameProfile.getName()));

                do {
                    do {
                        i = rand.nextInt();
                    } while (i == 0);
                } while (PowerManager.instance.frequncies.containsKey(i));

                xu2Tag.setInteger("Frequency", i);
                player.setPlayerNBT(playerNbt);
                PowerManager.instance.frequncies.put(i, gameProfile);
                PowerManager.instance.reassignValues();
                return i;
            }
        }
    }

}
