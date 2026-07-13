package me.moof.spawnrules.mixin;

import me.moof.spawnrules.config.ConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class EntityTypeMixin {

    @Inject(
            method = "create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnRequest;)Lnet/minecraft/world/entity/Entity;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onCreate(Level level, EntitySpawnRequest request, CallbackInfoReturnable<Entity> cir) {
        if (!level.isClientSide()) {
            EntitySpawnReason reason = request.reason();
            if (reason == EntitySpawnReason.NATURAL ||
                reason == EntitySpawnReason.CHUNK_GENERATION ||
                reason == EntitySpawnReason.SPAWNER ||
                reason == EntitySpawnReason.STRUCTURE ||
                reason == EntitySpawnReason.JOCKEY ||
                reason == EntitySpawnReason.PATROL ||
                reason == EntitySpawnReason.TRIAL_SPAWNER ||
                reason == EntitySpawnReason.REINFORCEMENT ||
                reason == EntitySpawnReason.EVENT ||
                reason == EntitySpawnReason.TRIGGERED ||
                reason == EntitySpawnReason.MOB_SUMMONED) {
                
                String dimension = level.dimension().identifier().toString();
                EntityType<?> type = (EntityType<?>)(Object)this;
                if (ConfigManager.isBlocked(type, dimension)) {
                    cir.setReturnValue(null);
                }
            }
        }
    }
}
