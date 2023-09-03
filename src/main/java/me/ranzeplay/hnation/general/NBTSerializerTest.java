package me.ranzeplay.hnation.general;

import me.ranzeplay.hnation.utils.NBTSerializable;
import me.ranzeplay.hnation.utils.NBTSerializeEntry;

import java.util.ArrayList;
import java.util.UUID;

@NBTSerializable
public class NBTSerializerTest {
    @NBTSerializeEntry
    int number;
    @NBTSerializeEntry
    double value;

    @NBTSerializeEntry
    ArrayList<UUID> arrayList;

    public NBTSerializerTest(int number, double value) {
        this.number = number;
        this.value = value;

        arrayList = new ArrayList<>();
        arrayList.add(UUID.randomUUID());
        arrayList.add(UUID.randomUUID());
        arrayList.add(UUID.randomUUID());
    }
}
