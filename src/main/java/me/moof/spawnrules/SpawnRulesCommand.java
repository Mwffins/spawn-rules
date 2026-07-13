package me.moof.spawnrules;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import me.moof.spawnrules.config.ConfigManager;
import me.moof.spawnrules.config.ConfigGuiMenu;

public class SpawnRulesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("spawnrules")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(
                    Commands.literal("reload")
                        .executes(context -> {
                            ConfigManager.load();
                            context.getSource().sendSuccess(
                                () -> Component.literal("Spawn Rules configuration reloaded!"), 
                                true
                            );
                            return 1;
                        })
                )
                .then(
                    Commands.literal("config")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.openMenu(new SimpleMenuProvider(
                                (containerId, inventory, p) -> new ConfigGuiMenu(containerId, inventory),
                                Component.literal("Spawn Rules Config")
                            ));
                            return 1;
                        })
                )
        );
    }
}
