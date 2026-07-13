package me.moof.spawnrules;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.moof.spawnrules.config.ConfigManager;

public class SpawnRules implements ModInitializer {
	public static final String MOD_ID = "spawn-rules";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Spawn Rules...");
		ConfigManager.load();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			SpawnRulesCommand.register(dispatcher);
		});
	}
}