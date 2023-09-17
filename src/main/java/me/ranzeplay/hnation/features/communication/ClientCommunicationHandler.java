package me.ranzeplay.hnation.features.communication;

import me.ranzeplay.hnation.features.communication.squad.client.SquadClientNetworking;
import me.ranzeplay.hnation.main.ClientMain;

import static me.ranzeplay.hnation.features.communication.messaging.global.client.ClientGlobalMessageNetworking.sendPublic;
import static me.ranzeplay.hnation.features.communication.squad.client.SquadClientNetworking.sendSquad;

public class ClientCommunicationHandler {
    public static void handle(String message) {
        switch(ClientMain.communicationFocusOption) {
            case CHANNEL -> {
            }
            case PUBLIC -> sendPublic(message);
            case PRIVATE -> {
            }
            case SQUAD -> sendSquad(message);
        }
    }

    public static void registerEvents() {
        SquadClientNetworking.registerEvents();
    }
}
