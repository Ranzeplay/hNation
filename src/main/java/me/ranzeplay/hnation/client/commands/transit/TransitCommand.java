package me.ranzeplay.hnation.client.commands.transit;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

@Environment(EnvType.CLIENT)
public class TransitCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("transit")
                .then(TransitLineCommand.buildCommandTree());
    }
}
