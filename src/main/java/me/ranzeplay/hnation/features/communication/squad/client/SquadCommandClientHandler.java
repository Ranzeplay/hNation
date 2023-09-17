package me.ranzeplay.hnation.features.communication.squad.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.ranzeplay.hnation.features.communication.squad.db.DbSquad;
import me.ranzeplay.hnation.features.player.db.DbPlayer;
import me.ranzeplay.hnation.main.ClientMain;
import me.ranzeplay.hnation.networking.SquadIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;
import java.util.UUID;

public class SquadCommandClientHandler {
    public static LiteralArgumentBuilder<FabricClientCommandSource> buildCommandTree() {
        return ClientCommandManager.literal("squad")
                .then(ClientCommandManager.literal("create")
                        .executes(context -> create())
                )
                .then(ClientCommandManager.literal("invite")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                                .executes(context -> invite(StringArgumentType.getString(context, "playerName"))
                                )
                        ))
                .then(ClientCommandManager.literal("kick")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                                .executes(context -> kick(StringArgumentType.getString(context, "playerName"))
                                )
                        ))
                .then(ClientCommandManager.literal("leave")
                        .executes(context -> leave())
                )
                .then(ClientCommandManager.literal("join")
                        .then(ClientCommandManager.argument("squadId", StringArgumentType.string())
                                .executes(context ->
                                        join(StringArgumentType.getString(context, "squadId"))
                                )
                        )
                )
                .then(ClientCommandManager.literal("dismiss")
                        .executes(context -> dismiss())
                )
                .then(ClientCommandManager.literal("warn")
                        .then(ClientCommandManager.argument("playerName", StringArgumentType.string())
                                .then(ClientCommandManager.argument("reason", StringArgumentType.string())
                                        .executes(context ->
                                                warn(StringArgumentType.getString(context, "playerName"),
                                                        StringArgumentType.getString(context, "reason")
                                                )
                                        )
                                )
                        )
                );
    }

    public static int create() {
        if (!isInSquad()) {
            var player = Objects.requireNonNull(MinecraftClient.getInstance().player);
            var squad = new DbSquad(new DbPlayer(player.getUuid(), player.getEntityName()));

            ClientPlayNetworking.send(SquadIdentifier.SQUAD_CREATE_REQUEST, PacketByteBufs.create().writeNbt(squad.toNbt()));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int join(String idString) {
        if (!isInSquad()) {
            var squadId = UUID.fromString(idString);
            ClientPlayNetworking.send(SquadIdentifier.SQUAD_JOIN_REQUEST, PacketByteBufs.create().writeUuid(squadId));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int leave() {
        if (isInSquad()) {
            ClientPlayNetworking.send(SquadIdentifier.SQUAD_LEAVE_REQUEST, PacketByteBufs.create());
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int invite(String playerName) {
        if (hasLeaderPermission()) {
            ClientPlayNetworking.send(SquadIdentifier.SQUAD_INVITE_REQUEST, PacketByteBufs.create().writeString(playerName));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int dismiss() {
        if (hasLeaderPermission()) {
            ClientPlayNetworking.send(SquadIdentifier.SQUAD_DISMISS_REQUEST, PacketByteBufs.create());
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int kick(String playerName) {
        if (hasLeaderPermission()) {
            ClientPlayNetworking.send(SquadIdentifier.SQUAD_KICK_REQUEST, PacketByteBufs.create().writeString(playerName));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int warn(String playerName, String reason) {
        if (hasLeaderPermission()) {
            ClientPlayNetworking.send(
                    SquadIdentifier.SQUAD_KICK_REQUEST,
                    PacketByteBufs.create()
                            .writeString(playerName)
                            .writeString(reason)
            );
        }

        return Command.SINGLE_SUCCESS;
    }

    @Environment(EnvType.CLIENT)
    private static boolean hasLeaderPermission() {
        if (isInSquad() && MinecraftClient.getInstance().player != null) {
            return ClientMain.joinedSquad.getLeaderId() == MinecraftClient.getInstance().player.getUuid();
        }

        return false;
    }

    @Environment(EnvType.CLIENT)
    private static boolean isInSquad() {
        return ClientMain.joinedSquad != null;
    }
}
