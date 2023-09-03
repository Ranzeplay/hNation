# Transit line

> The most complex part of hNation in development :)

The transit line, mostly railways. Will consider adding boat and elytra (refer to Airspace) transportation methods later.

## Create line

Command: `/hnt transit line create <name>`

Create a railway transport line with name of text in `<name>` parameter.

You should stand on the railway which is going become a railway line.

### Server-side behavior

The server will scan the wailway route from where you stand, that's why you should stand on a railway block (regardless of charged railway, normal railway, etc.).

The scan is based on DFS algorithm, if you have better ideas, we are pleased to reveice your contribution!

#### Why on server side?

Consider a long railway and a short view-distance on client. If the game client is unable to load all chunks the railway line passed, the search process will be interrupted. So it is a better choice to scan the route on server-side.

Consider a really long railway route, which world cost a long time to scan. So we use an individual thread to do this job. When the scan is complete, server will notify the player who submitted the creation of the railway.


