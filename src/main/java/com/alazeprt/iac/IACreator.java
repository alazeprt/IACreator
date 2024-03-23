package com.alazeprt.iac;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.ui.WelcomeUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IACreator {
    private static final Logger logger = LoggerFactory.getLogger(IACreator.class.toString());
    public static void main(String[] args) {
        logger.info("Launching IACreator v1.0.0-alpha.1...");
        ApplicationConfig.initialize();
        WelcomeUI.main(args);
    }
}
