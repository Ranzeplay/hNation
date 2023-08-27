package me.ranzeplay.hnation.client.commands;

import com.mojang.brigadier.Command;
import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import me.ranzeplay.hnation.networking.POICreationModel;
import me.ranzeplay.hnation.networking.POIQueryViewModel;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

public class POICommand {
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
