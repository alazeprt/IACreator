package com.alazeprt.iac;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.ui.WelcomeUI;

public class IACreator {
    public static void main(String[] args) {
        ApplicationConfig.initialize();
        WelcomeUI.main(args);
    }
}
