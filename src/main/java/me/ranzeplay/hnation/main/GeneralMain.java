package me.ranzeplay.hnation.main;

import me.ranzeplay.hnation.utils.NBTSerializerTest;
import me.ranzeplay.hnation.utils.NBTSerializer;
import me.ranzeplay.hnation.utils.NBTSerializerException;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralMain implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("hNation");

    @Override
    public void onInitialize() {
        LOGGER.info("hNation is initializing...");

        var obj = new NBTSerializerTest(1, 2.04);
        try {
            var res1 = NBTSerializer.serialize(obj);
            var res2 = NBTSerializer.deserialize(res1, new NBTSerializerTest(1, 2));

            LOGGER.info("Success");
        } catch (NBTSerializerException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
