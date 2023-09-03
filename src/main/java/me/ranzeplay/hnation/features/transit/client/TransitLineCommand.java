package me.ranzeplay.hnation.features.transit.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.main.NetworkingIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

@Environment(EnvType.CLIENT)
public class TransitLineCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("line")
                .then(ClientCommandManager.literal("create")
                        .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                .executes(context ->
                                        TransitLineCommand.createTransitLine(StringArgumentType.getString(context, "name"))
                                )
                        )
                );
    }

    public static int createTransitLine(String name) {
        ClientPlayNetworking.send(NetworkingIdentifier.CREATE_TRANSIT_LINE_REQUEST, PacketByteBufs.create().writeString(name));
        return Command.SINGLE_SUCCESS;
    }
}
