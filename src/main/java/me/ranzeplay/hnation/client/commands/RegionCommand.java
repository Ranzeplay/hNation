package me.ranzeplay.hnation.client.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import me.ranzeplay.hnation.networking.RegionCreationModel;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.joml.Vector2i;

public class RegionCommand {
    private static RegionCreationModel currentCreatingRegion = null;

    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("region")
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
                );
    }

    public static int createRegion(String name, String worldName, int minY, int maxY) {
        assert MinecraftClient.getInstance().player != null;
        if (currentCreatingRegion != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("There is already a region being created"));
        } else {
            currentCreatingRegion = new RegionCreationModel(name, worldName, maxY, minY);
            MinecraftClient.getInstance().player.sendMessage(Text.of("Go mark the border to create the region"));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int appendPoint(Vector2i point) {
        assert MinecraftClient.getInstance().player != null;
        if (currentCreatingRegion == null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("There's no region being created currently"));
        } else {
            currentCreatingRegion.appendPoint(point);
            MinecraftClient.getInstance().player.sendMessage(Text.of("Created a border point at (" + point.x + ", " + point.y + ")"));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int commitCreation() {
        assert MinecraftClient.getInstance().player != null;
        if (currentCreatingRegion == null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("There's no region being created currently"));
        } else {
            // Process
            var nbt = currentCreatingRegion.toNbt();
            ClientPlayNetworking.send(NetworkingIdentifier.CREATE_REGION_REQUEST, PacketByteBufs.create().writeNbt(nbt));
            MinecraftClient.getInstance().player.sendMessage(Text.of("Committing region creation"));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static void commitCreationReply(MinecraftClient client) {
        assert client.player != null;
        client.player.sendMessage(Text.of("Successfully created the region"));

        currentCreatingRegion = null;
    }

    public static int discard() {
        currentCreatingRegion = null;

        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.of("Discarded region creation"));

        return Command.SINGLE_SUCCESS;
    }
}
