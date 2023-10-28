package me.ranzeplay.hnation.features.communication.announcement.server;

import me.ranzeplay.hnation.features.communication.announcement.AnnouncementManager;
import me.ranzeplay.hnation.features.communication.announcement.db.DbAnnouncement;
import me.ranzeplay.hnation.features.communication.announcement.models.AnnouncementViewModel;
import me.ranzeplay.hnation.features.communication.announcement.models.CreateAnnouncementReplyModel;
import me.ranzeplay.hnation.features.communication.announcement.models.CreateAnnouncementRequestModel;
import me.ranzeplay.hnation.features.player.PlayerManager;
import me.ranzeplay.hnation.networking.AnnouncementIdentifier;
import me.ranzeplay.messagechain.managers.RemoteRouteManager;
import me.ranzeplay.messagechain.models.AbstractRouteExecutor;
import me.ranzeplay.messagechain.models.RouteHandler;
import me.ranzeplay.messagechain.models.RouteRequestContext;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerAnnouncementNetworking {
    public static void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(AnnouncementIdentifier.PULL_ANNOUNCEMENT_REQUEST,
                (_minecraftServer, sender, _serverPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> pushAnnouncements(sender, packetByteBuf)
        );

        RemoteRouteManager.getInstance().registerRoute(new RouteHandler<>(CreateAnnouncementRequestModel.class, CreateAnnouncementReplyModel.class, AnnouncementIdentifier.PUBLISH_ANNOUNCEMENT_REQUEST, new AnnoucementRequestHandler()));
    }

    static class AnnoucementRequestHandler extends AbstractRouteExecutor<CreateAnnouncementRequestModel, CreateAnnouncementReplyModel> {

        @Override
        public CreateAnnouncementReplyModel apply(RouteRequestContext<CreateAnnouncementRequestModel> context) {
            var sender = PlayerManager.getInstance().getPlayer(context.getPlayerSender());
            var announcement = new DbAnnouncement(context.getPayload().getTitle(), context.getPayload().getContent(), sender);
            var view = new AnnouncementViewModel(announcement);

            AnnouncementManager.getInstance().create(announcement);

            context.getServer().getPlayerManager().getPlayerList().forEach(p -> ServerPlayNetworking.send(p, AnnouncementIdentifier.BROADCAST_ANNOUNCEMENT_NOTIFY, PacketByteBufs.create().writeNbt(view.toNbt())));

            return new CreateAnnouncementReplyModel();
        }
    }

    private static void pushAnnouncements(ServerPlayerEntity sender, PacketByteBuf packetByteBuf) {
        var count = packetByteBuf.readShort();
        var result = new NbtList();
        var dbItems = AnnouncementManager.getInstance().getLatest(count);

        dbItems.stream()
                .map(AnnouncementViewModel::new)
                .forEach(a -> result.add(a.toNbt()));

        var nbt = new NbtCompound();
        nbt.put("announcements", result);
        ServerPlayNetworking.send(sender, AnnouncementIdentifier.PULL_ANNOUNCEMENT_REPLY, PacketByteBufs.create().writeNbt(nbt));
    }
}
