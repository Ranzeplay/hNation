package me.ranzeplay.hnation.features.transit.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.networking.TransitIdentifier;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ClientTransitNodeCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("node")
                .then(ClientCommandManager.literal("create")
                        .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                .then(ClientCommandManager.argument("regionName", StringArgumentType.string()))
                                .executes(context ->
                                        createTransitNode(StringArgumentType.getString(context, "name"), StringArgumentType.getString(context, "regionName"))
                                )
                        )
                );
    }

    private static int createTransitNode(String name, String regionName) {
        ClientPlayNetworking.send(TransitIdentifier.CREATE_TRANSIT_NODE_REQUEST, PacketByteBufs.create().writeString(name).writeString(regionName));
        return Command.SINGLE_SUCCESS;
    }
}
