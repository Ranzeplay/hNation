package me.ranzeplay.hnation.features.communication.mail.client;

import me.ranzeplay.hnation.features.communication.mail.viewmodel.SendMailViewModel;
import me.ranzeplay.hnation.networking.MailIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ClientMailCommandHandler {
    private static void sendMail(String receiverName, String title, String content) {
        var viewModel = new SendMailViewModel(receiverName, title, content);
        ClientPlayNetworking.send(MailIdentifier.SEND_MAIL_REQUEST, PacketByteBufs.create().writeNbt(viewModel.toNbt()));
    }
}
