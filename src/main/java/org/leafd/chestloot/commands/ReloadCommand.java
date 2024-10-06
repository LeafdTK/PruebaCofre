package org.leafd.chestloot.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.leafd.chestloot.ConfigManager;

import static net.minecraft.server.command.CommandManager.literal;

public class ReloadCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("chestloot")
                        .then(
                                literal("reload")
                                        .requires(source -> source.hasPermissionLevel(2))
                                        .executes(context -> {
                                            ConfigManager.reloadConfig();
                                            context.getSource().sendFeedback(
                                                    Text.literal("Configuración de ChestLoot recargada."),
                                                    true
                                            );
                                            return 1;
                                        })
                        )
        );
    }
}
