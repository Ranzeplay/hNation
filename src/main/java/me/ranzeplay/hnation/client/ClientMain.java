package me.ranzeplay.hnation.client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.ranzeplay.hnation.client.commands.POICommand;
import me.ranzeplay.hnation.client.commands.RegionCommand;
import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.joml.Vector2i;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerCommands();
        registerNetworkingHandlers();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hnt")
                    .then(ClientCommandManager.literal("poi")
                            .then(ClientCommandManager.literal("create")
                                    .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                            .executes(context ->
                                                    POICommand.create(context.getSource().getPlayer().getBlockX(),
                                                            context.getSource().getPlayer().getBlockY(),
                                                            context.getSource().getPlayer().getBlockZ(),
                                                            context.getSource().getPlayer().getWorld().getDimensionKey().getValue().toString(),
                                                            StringArgumentType.getString(context, "name"),
                                                            context.getSource().getPlayer().getUuid())))
                                    .then(ClientCommandManager.argument("x", IntegerArgumentType.integer())
                                            .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                                                    .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                                                            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                                                    .executes(context ->
                                                                            POICommand.create(IntegerArgumentType.getInteger(context, "x"),
                                                                                    IntegerArgumentType.getInteger(context, "y"),
                                                                                    IntegerArgumentType.getInteger(context, "z"),
                                                                                    context.getSource().getPlayer().getWorld().getDimensionKey().getValue().toString(),
                                                                                    StringArgumentType.getString(context, "name"),
                                                                                    context.getSource().getPlayer().getUuid())
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                            .then(ClientCommandManager.literal("query")
                                    .executes(context -> POICommand.query())
                            )
                    )
                    .then(ClientCommandManager.literal("region")
                            .then(ClientCommandManager.literal("create")
                                    .then(ClientCommandManager.literal("declare")
                                            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                                    .then(ClientCommandManager.argument("minY", IntegerArgumentType.integer())
                                                            .then(ClientCommandManager.argument("maxY", IntegerArgumentType.integer())
                                                                    .executes(context ->
                                                                            RegionCommand.createRegion(StringArgumentType.getString(context, "name"),
                                                                                    context.getSource().getPlayer().getWorld().getDimensionKey().getValue().toString(),
                                                                                    IntegerArgumentType.getInteger(context, "minY"),
                                                                                    IntegerArgumentType.getInteger(context, "maxY"))
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                                    .then(ClientCommandManager.literal("add")
                                            .executes(context ->
                                                    RegionCommand.appendPoint(new Vector2i(context.getSource().getPlayer().getBlockX(), context.getSource().getPlayer().getBlockZ()))
                                            )
                                    )
                                    .then(ClientCommandManager.literal("commit")
                                            .executes(context ->
                                                    RegionCommand.commitCreation()
                                            )
                                    )
                                    .then(ClientCommandManager.literal("discard")
                                            .executes(context ->
                                                    RegionCommand.discard()))
                            )
                    )
            );
        });
    }

    private void registerNetworkingHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.QUERY_POI_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, packetSender) -> POICommand.queryReply(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.CREATE_REGION_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, _packetByteBuf, _packetSender) -> RegionCommand.commitCreationReply(minecraftClient)
        );
    }
}
