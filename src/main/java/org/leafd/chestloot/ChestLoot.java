package org.leafd.chestloot;

import org.leafd.chestloot.commands.ReloadCommand;
import org.leafd.chestloot.events.ChestOpenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class ChestLoot implements ModInitializer {

    @Override
    public void onInitialize() {
        // Load configuration from JSON
        ConfigManager.loadConfig();

        // Register events
        UseBlockCallback.EVENT.register(new ChestOpenHandler());

        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ReloadCommand.register(dispatcher);
        });
    }
}
