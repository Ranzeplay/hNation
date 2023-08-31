package me.ranzeplay.hnation.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Arrays;

public class NBTSerializer {
    public static <T> NbtCompound serialize(T t) throws NBTSerializerException, IllegalAccessException {
        var clazz = t.getClass();
        if (clazz.isAnnotationPresent(NBTSerializable.class)) {
            var fieldsToSerialize = Arrays.stream(clazz.getDeclaredFields())
                    .takeWhile(f -> f.isAnnotationPresent(NBTSerializeEntry.class))
                    .toList();

            var result = new NbtCompound();
            for (var field : fieldsToSerialize) {
                field.setAccessible(true);
                var type = field.getType();

                if (type == String.class) {
                    result.putString(field.getName(), (String) field.get(t));
                } else if (type == boolean.class) {
                    result.putBoolean(field.getName(), (boolean) field.get(t));
                } else if (type == int.class) {
                    result.putInt(field.getName(), (int) field.get(t));
                } else if (type == byte.class) {
                    result.putByte(field.getName(), (byte) field.get(t));
                } else if (type == long.class) {
                    result.putLong(field.getName(), (long) field.get(t));
                } else if (type == float.class) {
                    result.putFloat(field.getName(), (float) field.get(t));
                } else if (type == double.class) {
                    result.putDouble(field.getName(), (double) field.get(t));
                } else if (type == byte[].class) {
                    result.putByteArray(field.getName(), (byte[]) field.get(t));
                } else if (type == int[].class) {
                    result.putIntArray(field.getName(), (int[]) field.get(t));
                } else if (type == long[].class) {
                    result.putLongArray(field.getName(), (long[]) field.get(t));
                } else if (type == ArrayList.class) {
                    var nbtList = new NbtList();

                    var list = (ArrayList<Object>) field.get(t);
                    for (var entry : list) {
                        var nbt = serialize(entry);
                        nbtList.add(nbt);
                    }

                    result.put(field.getName(), nbtList);
                } else {
                    var comp = serialize(field.get(t));
                    result.put(field.getName(), comp);
                }
            }

            return result;
        }

        throw new NBTSerializerException();
    }
}
