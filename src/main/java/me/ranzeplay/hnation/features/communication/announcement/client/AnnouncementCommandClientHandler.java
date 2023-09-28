package me.ranzeplay.hnation.features.communication.announcement.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.networking.AnnouncementIdentifier;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;

public class AnnouncementCommandClientHandler {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("announcement")
                .then(ClientCommandManager.literal("create")
                        .then(ClientCommandManager.argument("title", StringArgumentType.string())
                                .then(ClientCommandManager.argument("content", StringArgumentType.string())
                                        .executes(context -> create(StringArgumentType.getString(context, "title"), StringArgumentType.getString(context, "content")))
                                )
                        )
                );
    }

    private static int create(String title, String content) {
        var comp = new NbtCompound();
        comp.putString("title", title);
        comp.putString("content", content);
        ClientPlayNetworking.send(AnnouncementIdentifier.PUBLISH_ANNOUNCEMENT_REQUEST, PacketByteBufs.create().writeNbt(comp));

        return Command.SINGLE_SUCCESS;
    }
}
