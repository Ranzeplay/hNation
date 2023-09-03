# Squad

A small team consists of few players.

Messages will not be saved to server database, but they can be accessed by newly joined players via commands.

Player will be remove from the squad after they go offline for 5 minutes automatically. If the leader leaved the server, the squad will be transfered to a random player after 3 minutes.

Every squad has a leader, usually the player who creates the squad. The leader can invite, kick and warn players, also dismiss the squad.The leader can also transfer the leadership to another player in the squad.

All squads will be deleted when server closed, because they are all stored in memory.

## Create

Command: `/hnt squad create`

## Invite player

Command `/hnt squad invite <playerName>`

The invitation will be expired in 5 minutes.

If a team member invoked the command, it will be sent to team leader for the leader to check the invitation before sending to target player.

## Kick player

Command: `/hnt squad kick <playerName>`

The command can only be invoked by team leader

## Leave squad

Command: `/hnt squad leave`

Team leader can't leave the squad unless he/she has transfered leadership to another player.

## Dismiss squad

Command: `/hnt squad dismiss`

Just dismiss the squad, all players removed.

## Warn player

Command: `/hnt squad warn [message]`

Team leader check if the invoker is an ordinary team member. Team leader can invoke it directly.
