package me.ranzeplay.hnation.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;

public class POIQueryViewModel {
    int length;
    POIViewModel[] items;

    public POIQueryViewModel(POIViewModel[] items) {
        this.length = items.length;
        this.items = items;
    }

    public POIQueryViewModel() {
        length = 0;
    }

    public int getLength() {
        return length;
    }

    public POIViewModel[] getItems() {
        return items;
    }

    public void setItems(POIViewModel[] items) {
        this.length = items.length;
        this.items = items;
    }

    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putInt("length", length);

        NbtList list = new NbtList();
        for(var item : items) {
            list.add(item.toNbt());
        }

        nbt.put("items", list);

        return nbt;
    }

    public static POIQueryViewModel fromNbt(NbtCompound nbt) {
        var result = new POIQueryViewModel();
        result.length = nbt.getInt("length");

        var list = nbt.getList("items", NbtList.COMPOUND_TYPE);

        var items = new ArrayList<POIViewModel>();
        for (int i = 0; i < result.length; i++) {
            items.add(POIViewModel.fromNbt(list.getCompound(i)));
        }
        result.items = items.toArray(new POIViewModel[0]);

        return result;
    }
}
