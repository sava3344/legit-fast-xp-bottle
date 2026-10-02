package fastxp.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow private int itemUseCooldown;
    @Shadow public net.minecraft.client.network.ClientPlayerEntity player;

    @Inject(method = "doItemUse", at = @At("TAIL"))
    private void fastxp$shortenXpBottleDelay(CallbackInfo ci) {
        if (player == null) return;
        if (player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)
                || player.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
            // 25% -> 1 тик, 50% -> 2 тика, 25% -> 3 тика (в среднем 2)
            int roll = ThreadLocalRandom.current().nextInt(4);
            itemUseCooldown = roll == 0 ? 1 : (roll == 3 ? 3 : 2);
        }
    }
}
