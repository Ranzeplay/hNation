package me.ranzeplay.hnation.client.commands.transit;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class TransitLineCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("line")
                .then(ClientCommandManager.literal("create")
                        .executes(context -> TransitLineCommand.createTransitLine())
                );
    }

    public static int createTransitLine() {
        ClientPlayNetworking.send(NetworkingIdentifier.CREATE_TRANSIT_LINE_REQUEST, PacketByteBufs.create());
        return Command.SINGLE_SUCCESS;
    }
}
