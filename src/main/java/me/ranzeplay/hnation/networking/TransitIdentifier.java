package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class TransitIdentifier {
    public static final Identifier CREATE_TRANSIT_LINE_REQUEST = new Identifier("hnation.networking.request", "transit/line/create");
    public static final Identifier CREATE_TRANSIT_LINE_REPLY = new Identifier("hnation.networking.reply", "transit/line/create");

    public static final Identifier CREATE_TRANSIT_NODE_REQUEST = new Identifier("hnation.networking.request", "transit/node/create");
}
