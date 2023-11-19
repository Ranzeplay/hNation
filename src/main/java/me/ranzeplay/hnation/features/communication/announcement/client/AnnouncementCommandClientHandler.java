package me.ranzeplay.hnation.features.communication.announcement.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.features.communication.announcement.models.CreateAnnouncementReplyModel;
import me.ranzeplay.hnation.features.communication.announcement.models.CreateAnnouncementRequestModel;
import me.ranzeplay.hnation.networking.AnnouncementIdentifier;
import me.ranzeplay.messagechain.managers.routing.LocalRequestManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class AnnouncementCommandClientHandler {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("announcement")
                .then(ClientCommandManager.literal("create")
                        .then(ClientCommandManager.argument("title", StringArgumentType.string())
                                .then(ClientCommandManager.argument("content", StringArgumentType.string())
                                        .executes(context -> create(StringArgumentType.getString(context, "title"), StringArgumentType.getString(context, "content")))
                                )
                        )
                        .executes(context -> {
                            var client = MinecraftClient.getInstance();
                            client.send(() -> client.setScreen(new AnnouncementCreationScreen()));
                            return Command.SINGLE_SUCCESS;
                        })
                );
    }

    protected static int create(String title, String content) {
        LocalRequestManager.getInstance().sendThreadedRequest(AnnouncementIdentifier.PUBLISH_ANNOUNCEMENT_REQUEST, new CreateAnnouncementRequestModel(title, content), CreateAnnouncementReplyModel.class, x -> {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Successfully published the announcement!"));
        });
        return Command.SINGLE_SUCCESS;
    }
}
