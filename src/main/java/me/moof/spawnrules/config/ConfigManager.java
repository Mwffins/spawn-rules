package me.moof.spawnrules.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import me.moof.spawnrules.SpawnRules;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("spawnrules.json");
    private static SpawnRulesConfig config = new SpawnRulesConfig();

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    config = GSON.fromJson(reader, SpawnRulesConfig.class);
                    if (config == null) {
                        config = new SpawnRulesConfig();
                    }
                }
            } else {
                config = new SpawnRulesConfig();
                save();
            }
        } catch (Exception e) {
            SpawnRules.LOGGER.error("Failed to load spawn rules config", e);
            config = new SpawnRulesConfig();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (Exception e) {
            SpawnRules.LOGGER.error("Failed to save spawn rules config", e);
        }
    }

    public static boolean isBlocked(EntityType<?> type, String dimension) {
        if (config == null || config.dimensions == null) {
            return false;
        }
        
        SpawnRulesConfig.DimensionConfig dimConfig = config.dimensions.get(dimension);
        if (dimConfig == null) {
            return false;
        }

        String typeId = BuiltInRegistries.ENTITY_TYPE.getKey(type).toString();
        if (dimConfig.blockedTypes != null && dimConfig.blockedTypes.contains(typeId)) {
            return true;
        }

        String categoryName = type.getCategory().getName();
        if (dimConfig.blockedCategories != null && dimConfig.blockedCategories.contains(categoryName)) {
            return true;
        }

        return false;
    }

    public static boolean isMobBlocked(EntityType<?> type, String dimension) {
        if (config == null || config.dimensions == null) {
            return false;
        }
        SpawnRulesConfig.DimensionConfig dimConfig = config.dimensions.get(dimension);
        if (dimConfig == null) {
            return false;
        }
        String typeId = BuiltInRegistries.ENTITY_TYPE.getKey(type).toString();
        return dimConfig.blockedTypes != null && dimConfig.blockedTypes.contains(typeId);
    }

    public static boolean isCategoryBlocked(String category, String dimension) {
        if (config == null || config.dimensions == null) {
            return false;
        }
        SpawnRulesConfig.DimensionConfig dimConfig = config.dimensions.get(dimension);
        if (dimConfig == null) {
            return false;
        }
        return dimConfig.blockedCategories != null && dimConfig.blockedCategories.contains(category);
    }

    public static void toggleMob(EntityType<?> type, String dimension) {
        if (config == null) {
            config = new SpawnRulesConfig();
        }
        if (config.dimensions == null) {
            config.dimensions = new HashMap<>();
        }

        SpawnRulesConfig.DimensionConfig dimConfig = config.dimensions.computeIfAbsent(dimension, k -> new SpawnRulesConfig.DimensionConfig());
        if (dimConfig.blockedTypes == null) {
            dimConfig.blockedTypes = new ArrayList<>();
        }

        String typeId = BuiltInRegistries.ENTITY_TYPE.getKey(type).toString();
        if (dimConfig.blockedTypes.contains(typeId)) {
            dimConfig.blockedTypes.remove(typeId);
        } else {
            dimConfig.blockedTypes.add(typeId);
        }
        save();
    }

    public static void toggleCategory(String category, String dimension) {
        if (config == null) {
            config = new SpawnRulesConfig();
        }
        if (config.dimensions == null) {
            config.dimensions = new HashMap<>();
        }

        SpawnRulesConfig.DimensionConfig dimConfig = config.dimensions.computeIfAbsent(dimension, k -> new SpawnRulesConfig.DimensionConfig());
        if (dimConfig.blockedCategories == null) {
            dimConfig.blockedCategories = new ArrayList<>();
        }

        if (dimConfig.blockedCategories.contains(category)) {
            dimConfig.blockedCategories.remove(category);
        } else {
            dimConfig.blockedCategories.add(category);
        }
        save();
    }
}
