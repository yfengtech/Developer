package cz.developer.library;

import cz.developer.library.ui.switchs.ISwitchInterface;

/**
 * Created by Administrator on 2016/11/7.
 */

public class DeveloperConfig {
    public ISwitchInterface switchConfig;
    private DeveloperConfig(){
    }


    public static class Builder{
        private DeveloperConfig config;
        public Builder() {
            this.config = new DeveloperConfig();
        }

        public Builder setSwitchInterface(ISwitchInterface switchConfig){
            this.config.switchConfig =switchConfig;
            return this;
        }

        public DeveloperConfig build(){
            return this.config;
        }
    }
}
