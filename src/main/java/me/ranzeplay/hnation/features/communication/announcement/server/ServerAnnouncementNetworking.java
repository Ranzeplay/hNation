package me.ranzeplay.hnation.features.communication.announcement.server;

import me.ranzeplay.hnation.features.communication.announcement.AnnouncementManager;
import me.ranzeplay.hnation.features.communication.announcement.viewmodel.AnnouncementViewModel;
import me.ranzeplay.hnation.networking.AnnouncementIdentifier;
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
