package me.ranzeplay.hnation.networking;

import net.minecraft.util.Identifier;

public class MailIdentifier {
    public static final Identifier SEND_MAIL_REQUEST = new Identifier("hnation.networking.request", "mail/send");
    public static final Identifier SEND_MAIL_NOTIFY = new Identifier("hnation.networking.notify", "mail/send");
    public static final Identifier RECEIVE_MAIL_NOTIFY = new Identifier("hnation.networking.notify", "mail/receive");
    public static final Identifier UPDATE_MAIL_STATUS_REQUEST = new Identifier("hnation.networking.request", "mail/update_status");
    public static final Identifier UPDATE_MAIL_STATUS_REPLY = new Identifier("hnation.networking.reply", "mail/update_status");
    public static final Identifier GET_MAIL_REQUEST = new Identifier("hnation.networking.reply", "mail/update_status");
    public static final Identifier GET_MAIL_REPLY = new Identifier("hnation.networking.reply", "mail/update_status");
}
