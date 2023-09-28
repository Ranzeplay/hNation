package me.ranzeplay.hnation.features.communication.announcement.server;

import me.ranzeplay.hnation.features.communication.announcement.AnnouncementManager;
import me.ranzeplay.hnation.features.communication.announcement.db.DbAnnouncement;
import me.ranzeplay.hnation.features.communication.announcement.viewmodel.AnnouncementViewModel;
import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.networking.AnnouncementIdentifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class AnnouncementCommandServerHandler {
    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(AnnouncementIdentifier.PUBLISH_ANNOUNCEMENT_REQUEST,
                (minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> create(minecraftServer, sender, packetByteBuf));
    }

    private static void create(MinecraftServer server, ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var comp = Objects.requireNonNull(packetByteBuf.readNbt());
        var announcement = new DbAnnouncement(comp.getString("title"), comp.getString("content"), PlayerManager.getInstance().getPlayer(sender));
        var view = new AnnouncementViewModel(announcement);

        AnnouncementManager.getInstance().create(announcement);

        var response = PacketByteBufs.create();
        response.writeBoolean(true);
        ServerPlayNetworking.send(sender, AnnouncementIdentifier.PUBLISH_ANNOUNCEMENT_NOTIFY, response);

        server.getPlayerManager().getPlayerList().forEach(p -> ServerPlayNetworking.send(p, AnnouncementIdentifier.BROADCAST_ANNOUNCEMENT_NOTIFY, PacketByteBufs.create().writeNbt(view.toNbt())));
    }
}
