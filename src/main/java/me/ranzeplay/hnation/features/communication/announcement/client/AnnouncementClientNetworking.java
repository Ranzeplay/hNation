package me.ranzeplay.hnation.features.communication.announcement.client;

import me.ranzeplay.hnation.features.communication.announcement.models.AnnouncementViewModel;
import me.ranzeplay.hnation.networking.AnnouncementIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Objects;

public class AnnouncementClientNetworking {
    public static void registerEvents() {
        ClientPlayConnectionEvents.JOIN.register((_clientPlayNetworkHandler, _packetSender, _minecraftClient) -> {
            pullLatestAnnouncements();
        });

        ClientPlayNetworking.registerGlobalReceiver(AnnouncementIdentifier.PULL_ANNOUNCEMENT_REPLY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> receiveLatestAnnouncements(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(AnnouncementIdentifier.PUBLISH_ANNOUNCEMENT_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> publishStatus(minecraftClient, packetByteBuf)
        );

        ClientPlayNetworking.registerGlobalReceiver(AnnouncementIdentifier.BROADCAST_ANNOUNCEMENT_NOTIFY,
                (minecraftClient, _clientPlayNetworkHandler, packetByteBuf, _packetSender)
                        -> receiveBroadcast(minecraftClient, packetByteBuf)
        );
    }

    private static void receiveBroadcast(MinecraftClient client, PacketByteBuf packetByteBuf) {
        var announcement = new AnnouncementViewModel(Objects.requireNonNull(packetByteBuf.readNbt()));
        assert client.player != null;
        client.player.sendMessage(Text.literal(announcement.toString()));
    }

    private static void publishStatus(MinecraftClient client, PacketByteBuf packetByteBuf) {
        assert client.player != null;
        client.player.sendMessage(Text.literal("Announcement publish status: " + (packetByteBuf.readBoolean() ? "success" : "fail")));
    }

    private static void pullLatestAnnouncements() {
        var packet = PacketByteBufs.create();
        packet.writeShort(5);
        ClientPlayNetworking.send(AnnouncementIdentifier.PULL_ANNOUNCEMENT_REQUEST, packet);
    }

    private static void receiveLatestAnnouncements(MinecraftClient client, PacketByteBuf packetByteBuf) {
        assert client.player != null;
        var data = Objects.requireNonNull(packetByteBuf.readNbt());
        var dataList = data.getList("announcements", NbtElement.COMPOUND_TYPE);
        client.player.sendMessage(Text.literal(String.format("Displaying latest %d announcement(s)", dataList.size())));
        for (int i = 0; i < dataList.size(); i++) {
            var entry = dataList.getCompound(i);
            var announcement = new AnnouncementViewModel(entry);
            client.player.sendMessage(Text.literal(announcement.toString()));
        }
    }
}
