package com.alazeprt.iac;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.ui.WelcomeUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IACreator {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        logger.info("Launching IACreator...");
        ApplicationConfig.initialize();
        WelcomeUI.main(args);
    }
}
