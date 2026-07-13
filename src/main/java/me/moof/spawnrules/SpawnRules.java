package me.moof.spawnrules;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpawnRules implements ModInitializer {
	public static final String MOD_ID = "spawn-rules";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing spawn rules");
	}
}