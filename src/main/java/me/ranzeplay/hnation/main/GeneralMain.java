package me.ranzeplay.hnation.main;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralMain implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("hNation");

    @Override
    public void onInitialize() {
        LOGGER.info("hNation is initializing...");
    }
}
