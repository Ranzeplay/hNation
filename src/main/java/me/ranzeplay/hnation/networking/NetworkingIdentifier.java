package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class NetworkingIdentifier {
    public static final Identifier CREATE_POI_REQUEST = new Identifier("hnation.networking.request", "poi/create");
    public static final Identifier CREATE_POI_REPLY = new Identifier("hnation.networking.reply", "poi/create");

    public static final Identifier QUERY_POI_REQUEST = new Identifier("hnation.networking.request", "poi/query");
    public static final Identifier QUERY_POI_REPLY = new Identifier("hnation.networking.reply", "poi/query");

    public static final Identifier CREATE_REGION_REQUEST = new Identifier("hnation.networking.request", "region/create");
    public static final Identifier CREATE_REGION_REPLY = new Identifier("hnation.networking.reply", "region/create");
}
