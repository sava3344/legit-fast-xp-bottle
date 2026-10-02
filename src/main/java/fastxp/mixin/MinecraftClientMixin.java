package fastxp.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow private int itemUseCooldown;
    @Shadow public net.minecraft.client.network.ClientPlayerEntity player;

    // doItemUse() ставит itemUseCooldown = 4 в начале. После него, если в руках
    // бутылочка опыта, уменьшаем задержку до 2 тиков. Другие предметы не трогаем.
    @Inject(method = "doItemUse", at = @At("TAIL"))
    private void fastxp$shortenXpBottleDelay(CallbackInfo ci) {
        if (player == null) return;
        if (player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)
                || player.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
            if (itemUseCooldown > 2) itemUseCooldown = 2;
        }
    }
}
