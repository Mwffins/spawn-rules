package me.moof.spawnrules.mixin;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.moof.spawnrules.config.ConfigManager;

@Mixin(SpawnPlacements.class)
public class SpawnPlacementsMixin {

    @Inject(
            method = "checkSpawnRules",
            at = @At("HEAD"),
            cancellable = true
    )
    private static <T extends Entity> void onCheckSpawnRules(
            EntityType<T> type,
            ServerLevelAccessor level,
            EntitySpawnReason spawnReason,
            BlockPos pos,
            RandomSource random,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (spawnReason != EntitySpawnReason.LOAD) {
            String dimension = level.getLevel().dimension().identifier().toString();
            if (ConfigManager.isBlocked(type, dimension)) {
                cir.setReturnValue(false);
            }
        }
    }
}
