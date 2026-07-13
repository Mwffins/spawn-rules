package me.moof.spawnrules.config;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnRulesConfig {
    public Map<String, DimensionConfig> dimensions = new HashMap<>();

    public static class DimensionConfig {
        @SerializedName("blocked_types")
        public List<String> blockedTypes = new ArrayList<>();

        @SerializedName("blocked_categories")
        public List<String> blockedCategories = new ArrayList<>();
    }
}
