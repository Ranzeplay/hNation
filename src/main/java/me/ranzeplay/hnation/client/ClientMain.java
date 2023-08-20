package me.ranzeplay.hnation.client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.ranzeplay.hnation.client.commands.POICommand;
import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.command.argument.NumberRangeArgumentType;

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
                                    .then(ClientCommandManager.argument("x", NumberRangeArgumentType.intRange())
                                            .then(ClientCommandManager.argument("y", NumberRangeArgumentType.intRange())
                                                    .then(ClientCommandManager.argument("z", NumberRangeArgumentType.intRange())
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
            );
        });
    }

    private void registerNetworkingHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingIdentifier.QUERY_POI_REPLY,
                (minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> POICommand.queryReply(packetByteBuf)
        );
    }
}
