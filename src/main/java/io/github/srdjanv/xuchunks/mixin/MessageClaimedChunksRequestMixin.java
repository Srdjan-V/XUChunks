package io.github.srdjanv.xuchunks.mixin;

import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftbutilities.net.MessageClaimedChunksRequest;
import io.github.srdjanv.xuchunks.handelers.TeamPowerManager;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MessageClaimedChunksRequest.class, remap = false)
public abstract class MessageClaimedChunksRequestMixin extends MessageToServer {
    public MessageClaimedChunksRequestMixin() {
        throw new AssertionError();
    }

    @Inject(method = "onMessage", at = @At(value = "HEAD"))
    public void onMessage(EntityPlayerMP player, CallbackInfo ci) {
        TeamPowerManager.tickTeamPower(player);
    }
}
