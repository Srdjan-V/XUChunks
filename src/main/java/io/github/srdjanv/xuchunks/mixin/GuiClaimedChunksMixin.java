package io.github.srdjanv.xuchunks.mixin;

import com.feed_the_beast.ftblib.lib.gui.misc.GuiChunkSelectorBase;
import com.feed_the_beast.ftbutilities.gui.GuiClaimedChunks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GuiClaimedChunks.class, remap = false)
public abstract class GuiClaimedChunksMixin extends GuiChunkSelectorBase {
    public GuiClaimedChunksMixin() {
        throw new AssertionError();
    }

    @Shadow
    private static int xUChunks$maxClaimedChunks;
    @Shadow
    private static int xUChunks$maxLoadedChunks;

    @Inject(method = "addCornerText", at = @At(value = "TAIL"))
    private void inject$addCornerText(List<String> list, GuiChunkSelectorBase.Corner corner, CallbackInfo ci) {
        if (corner == Corner.BOTTOM_RIGHT) {
            int line = list.size() - 2;
            list.add(line, "XUGridPower maxClaimedChunks: " + xUChunks$maxClaimedChunks);
            list.add(line, "XUGridPower maxLoadedChunks: " + xUChunks$maxLoadedChunks);
        }
    }
}
