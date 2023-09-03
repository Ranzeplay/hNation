package me.ranzeplay.hnation.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

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
                } else if(type == short.class) {
                    result.putShort(field.getName(), (short) field.get(t));
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
                } else if (type == UUID.class) {
                    result.putUuid(field.getName(), (UUID) field.get(t));
                } else {
                    var comp = serialize(field.get(t));
                    result.put(field.getName(), comp);
                }
            }

            return result;
        } else {

        }

        throw new NBTSerializerException();
    }

    public static <T> T deserialize(NbtCompound nbt, T obj) throws NBTSerializerException, IllegalAccessException, InstantiationException {
        var clazz = obj.getClass();
        if(clazz.isAnnotationPresent(NBTSerializable.class)) {
            var fieldsToSerialize = Arrays.stream(clazz.getDeclaredFields())
                    .takeWhile(f -> f.isAnnotationPresent(NBTSerializeEntry.class))
                    .toList();

            for(var field : fieldsToSerialize) {
                field.setAccessible(true);

                var type = field.getType();
                if (type == String.class) {
                    field.set(obj, nbt.getString(field.getName()));;
                } else if (type == boolean.class) {
                    field.set(obj, nbt.getBoolean(field.getName()));;
                } else if(type == short.class) {
                    field.set(obj, nbt.getShort(field.getName()));;
                } else if (type == int.class) {
                    field.set(obj, nbt.getInt(field.getName()));;
                } else if (type == byte.class) {
                    field.set(obj, nbt.getByte(field.getName()));;
                } else if (type == long.class) {
                    field.set(obj, nbt.getLong(field.getName()));;
                } else if (type == float.class) {
                    field.set(obj, nbt.getFloat(field.getName()));;
                } else if (type == double.class) {
                    field.set(obj, nbt.getDouble(field.getName()));;
                } else if (type == byte[].class) {
                    field.set(obj, nbt.getByteArray(field.getName()));;
                } else if (type == int[].class) {
                    field.set(obj, nbt.getIntArray(field.getName()));;
                } else if (type == long[].class) {
                    field.set(obj, nbt.getLongArray(field.getName()));;
                } else if (type == ArrayList.class) {
                    var nbtList = nbt.getList(field.getName(), NbtElement.COMPOUND_TYPE);

                    var listEntryType = type.getGenericInterfaces()[0];
                    var list = ArrayList.class.newInstance();
                    for (int i = 0; i < nbtList.size(); i++) {
                        var comp = nbtList.getCompound(i);
                        var deserializedObj = deserialize(comp, listEntryType.getClass());
                        list.add(deserializedObj);
                    }

                    field.set(field.getName(), list);
                } else if (type == UUID.class) {
                    field.set(obj, nbt.getUuid(field.getName()));
                } else {
                    var comp = deserialize(nbt.getCompound(field.getName()), field.getType());
                    field.set(field.getName(), comp);
                }
            }
        }

        throw new NBTSerializerException();
    }
}
