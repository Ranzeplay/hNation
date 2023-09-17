package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class SquadIdentifier {
    public static final Identifier SQUAD_CREATE_REQUEST = new Identifier("hnation.networking.request", "squad/create");
    public static final Identifier SQUAD_CREATE_REPLY = new Identifier("hnation.networking.reply", "squad/create");
    public static final Identifier SQUAD_JOIN_REQUEST = new Identifier("hnation.networking.request", "squad/join");
    public static final Identifier SQUAD_JOIN_REQUEST_NOTIFY = new Identifier("hnation.networking.notify", "squad/join");
    public static final Identifier SQUAD_JOIN_REACTION = new Identifier("hnation.networking.request", "squad/join");
    public static final Identifier SQUAD_KICK_REQUEST = new Identifier("hnation.networking.request", "squad/kick");
    public static final Identifier SQUAD_KICK_NOTIFY = new Identifier("hnation.networking.notify", "squad/kick");
    public static final Identifier SQUAD_INVITE_REQUEST = new Identifier("hnation.networking.request", "squad/kick");
    public static final Identifier SQUAD_INVITE_NOTIFY = new Identifier("hnation.networking.request", "squad/kick");
    public static final Identifier SQUAD_LEAVE_REQUEST = new Identifier("hnation.networking.request", "squad/leave");
    public static final Identifier SQUAD_LEAVE_NOTIFY = new Identifier("hnation.networking.notify", "squad/leave");
    public static final Identifier SQUAD_TRANSFER_REQUEST = new Identifier("hnation.networking.request", "squad/transfer");
    public static final Identifier SQUAD_TRANSFER_NOTIFY = new Identifier("hnation.networking.notify", "squad/transfer");
    public static final Identifier SQUAD_DISMISS_REQUEST = new Identifier("hnation.networking.request", "squad/dismiss");
    public static final Identifier SQUAD_DISMISS_NOTIFY = new Identifier("hnation.networking.notify", "squad/dismiss");
    public static final Identifier SQUAD_WARN_REQUEST = new Identifier("hnation.networking.request", "squad/warn");
    public static final Identifier SQUAD_WARN_NOTIFY = new Identifier("hnation.networking.notify", "squad/warn");
    public static final Identifier SQUAD_MESSAGE_NOTIFY = new Identifier("hnation.networking.notify", "squad/message");
}
