package me.moof.spawnrules.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.SpawnEggItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityFilter {
    public static List<EntityType<?>> getSpawnableMobs() {
        List<EntityType<?>> list = new ArrayList<>();
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (type == EntityTypes.GIANT || type == EntityTypes.ILLUSIONER || type == EntityTypes.PLAYER) {
                continue;
            }

            if (SpawnEggItem.byId(type).isPresent() || 
                type.getCategory() != MobCategory.MISC ||
                type == EntityTypes.IRON_GOLEM ||
                type == EntityTypes.SNOW_GOLEM ||
                type == EntityTypes.VILLAGER) {
                list.add(type);
            }
        }
        list.sort(Comparator.comparing(type -> BuiltInRegistries.ENTITY_TYPE.getKey(type).toString()));
        return list;
    }
}
