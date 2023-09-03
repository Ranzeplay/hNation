package me.ranzeplay.hnation.features.communication;

import me.ranzeplay.hnation.main.ClientMain;

import static me.ranzeplay.hnation.features.communication.messaging.global.client.GlobalMessageClientHandler.sendPublic;

public class ClientCommunicationHandler {
    public static void handle(String message) {
        switch(ClientMain.communicationFocusOption) {

            case CHANNEL -> {
            }
            case PUBLIC -> sendPublic(message);
            case PRIVATE -> {
            }
            case SQUAD -> {
            }
        }
    }
}
