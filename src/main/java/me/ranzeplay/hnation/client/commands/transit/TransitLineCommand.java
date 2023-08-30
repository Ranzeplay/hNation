package me.ranzeplay.hnation.client.commands.transit;

import com.mojang.brigadier.Command;
import me.ranzeplay.hnation.networking.NetworkingIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class TransitLineCommand {
    public static int createTransitLine() {
        ClientPlayNetworking.send(NetworkingIdentifier.CREATE_TRANSIT_LINE_REQUEST, PacketByteBufs.create());
        return Command.SINGLE_SUCCESS;
    }
}
