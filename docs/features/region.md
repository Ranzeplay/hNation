# Region

A space with virtual border that can be assigned to different usages.

## Create

### Step 1: Initiate creation process

**Command:** `/hnt region create declare <name> <minY> <maxY>`

#### Parameters

`minY`: The minimal value on Y axis of the region to create

`maxY`: The maximum value on Y axis of the region to create

### Step 2: Add border points

**Command:**  `/hnt region create add`

Add a border point at where player stands

Re-run this commands until you marked all the border points

### Step 3: Commit changes

**Command:** `/hnt region create commit`

Finish the creation process, the region will be send and save to server database

### Discard changes

**Command:** `/hnt region create discard`

Cancel the operation

## Role allocation

**Command:** `/hnt region role set <roleName>`

Allocate region a role so that you can better control it.

Such as `TransitNode` and `StoragePool`
