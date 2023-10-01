package me.ranzeplay.hnation.features.communication;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.features.communication.messaging.client.ClientDirectMessageNetworking;
import me.ranzeplay.hnation.features.communication.squad.client.SquadClientNetworking;
import me.ranzeplay.hnation.main.ClientMain;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static me.ranzeplay.hnation.features.communication.messaging.client.ClientDirectMessageNetworking.sendDirect;
import static me.ranzeplay.hnation.features.communication.messaging.global.client.ClientGlobalMessageNetworking.sendPublic;
import static me.ranzeplay.hnation.features.communication.squad.client.SquadClientNetworking.sendSquadMessage;

public class ClientCommunicationHandler {
    public static void handle(String message) {
        switch (ClientMain.communicationFocusOption) {
            case CHANNEL -> {
            }
            case GLOBAL -> sendPublic(message);
            case DIRECT -> sendDirect(ClientMain.communicationFocusOption.getCommId(), message);
            case SQUAD -> sendSquadMessage(message);
        }
    }

    public static void registerEvents() {
        SquadClientNetworking.registerEvents();
        ClientDirectMessageNetworking.registerEvents();
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("chat")
                .then(ClientCommandManager.literal("focus")
                        .then(ClientCommandManager.literal("global")
                                .executes(context -> {
                                    ClientMain.communicationFocusOption = CommunicationFocusOption.GLOBAL;
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(ClientCommandManager.literal("direct")
                                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                                        .executes(context -> {
                                            ClientMain.communicationFocusOption = CommunicationFocusOption.DIRECT;
                                            ClientMain.communicationFocusOption.setCommId(StringArgumentType.getString(context, "player"));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("squad")
                                .executes(context -> {
                                    ClientMain.communicationFocusOption = CommunicationFocusOption.SQUAD;
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                );
    }
}
