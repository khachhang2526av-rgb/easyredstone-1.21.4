package net.easyhopper.mixin;

import net.easyhopper.EasyHopperMod;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    private void easyredstone$forceSneak(CallbackInfoReturnable<Boolean> cir) {
        if (EasyHopperMod.isEnabled() && ClientPlayerInteractionManagerMixin.isFakeSneaking()) {
            cir.setReturnValue(true);
        }
    }
}
