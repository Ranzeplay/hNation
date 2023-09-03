package me.ranzeplay.hnation.features.poi.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.main.NetworkingIdentifier;
import me.ranzeplay.hnation.features.poi.viewmodel.POICreationModel;
import me.ranzeplay.hnation.features.poi.viewmodel.POIQueryViewModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ClientPoiCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("poi")
                .then(ClientCommandManager.literal("create")
                        .then(ClientCommandManager.literal("here")
                                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                        .executes(context ->
                                                ClientPoiCommand.create(context.getSource().getPlayer().getBlockX(),
                                                        context.getSource().getPlayer().getBlockY(),
                                                        context.getSource().getPlayer().getBlockZ(),
                                                        context.getSource().getPlayer().getWorld().getDimensionKey().getValue().toString(),
                                                        StringArgumentType.getString(context, "name"),
                                                        context.getSource().getPlayer().getUuid())
                                        )
                                )
                        )
                        .then(ClientCommandManager.literal("at")
                                .then(ClientCommandManager.argument("x", IntegerArgumentType.integer())
                                        .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                                                .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                                                        .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                                                .executes(context ->
                                                                        ClientPoiCommand.create(IntegerArgumentType.getInteger(context, "x"),
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
                )
                .then(ClientCommandManager.literal("query")
                        .executes(context -> ClientPoiCommand.query())
                );
    }

    public static int create(int x, int y, int z, String worldName, String name, UUID playerUuid) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.of("Creating POI \"" + name + "\" at [" + x + ", " + y + ", " + z + " @ " + worldName + "]"));

        var model = new POICreationModel(x, y, z, worldName, name, playerUuid);
        ClientPlayNetworking.send(NetworkingIdentifier.CREATE_POI_REQUEST, PacketByteBufs.create().writeNbt(model.toNbt()));

        return Command.SINGLE_SUCCESS;
    }

    public static int query() {
        ClientPlayNetworking.send(NetworkingIdentifier.QUERY_POI_REQUEST, PacketByteBufs.create());
        return Command.SINGLE_SUCCESS;
    }

    public static void queryReply(MinecraftClient client, PacketByteBuf buf) {
        var model = POIQueryViewModel.fromNbt(Objects.requireNonNull(buf.readNbt()));

        assert client.player != null;
        client.player.sendMessage(Text.of("Showing " + model.getLength() + " POI item(s)"));
        for (var item : model.getItems()){
           client.player.sendMessage(Text.of(item.getName() + " at [" + item.getX() + ", " + item.getY() + ", " + item.getZ() + " @ " + item.getWorldName() + "]"));
        }
    }
}
