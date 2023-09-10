package me.ranzeplay.hnation.features.communication.squad.tasks;

import me.ranzeplay.hnation.main.ServerMain;

import java.util.TimerTask;
import java.util.UUID;

public class SquadDismissTask extends TimerTask {
    UUID squadId;

    public SquadDismissTask(UUID squadId) {
        this.squadId = squadId;
    }

    @Override
    public void run() {
        ServerMain.squadManager.dismissSquad(squadId);
    }
}
