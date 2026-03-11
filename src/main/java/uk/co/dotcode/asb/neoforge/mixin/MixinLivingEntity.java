package uk.co.dotcode.asb.neoforge.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.dotcode.asb.ModUtils;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    public int asbTickCount = 0;

    // Remove this field initialization
    // public LivingEntity asbCurrentInstance = (LivingEntity)this;

    private LivingEntity getAsbCurrentInstance() {
        return (LivingEntity)(Object)this; // safe cast in Mixin
    }

    @Inject(at = @At("HEAD"), method = "tickEffects()V", cancellable = true)
    public void tickEffects(CallbackInfo ci) {
        ++asbTickCount;
        if (asbTickCount >= 10) {
            LivingEntity instance = getAsbCurrentInstance();
            ModUtils.checkAndApplyBonusesTo(instance, instance, "self");
            ModUtils.checkAndApplyImmunitiesTo(instance, instance);
            asbTickCount = 0;
        }
    }

    @Inject(at = @At("RETURN"), method = "canBeAffected(Lnet/minecraft/world/effect/MobEffectInstance;)Z", cancellable = true)
    public void canBeAffected(MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            LivingEntity instance = getAsbCurrentInstance();
            boolean isImmune = ModUtils.isImmuneToEffect(mobEffectInstance, instance);
            cir.setReturnValue(!isImmune);
        }
    }
}
