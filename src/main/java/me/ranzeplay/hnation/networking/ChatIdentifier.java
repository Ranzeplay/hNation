package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class ChatIdentifier {
    public static final Identifier SEND_CHAT_GLOBAL = new Identifier("hnation.networking.chat", "send/global");
    public static final Identifier SEND_CHAT_DIRECT = new Identifier("hnation.networking.chat", "send/direct");
    public static final Identifier RECEIVE_CHAT_DIRECT = new Identifier("hnation.networking.chat", "receive/direct");
    public static final Identifier SEND_CHAT_CHANNEL = new Identifier("hnation.networking.chat", "send/channel");
    public static final Identifier SEND_CHAT_SQUAD = new Identifier("hnation.networking.chat", "send/squad");
}
