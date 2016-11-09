package cz.developer.library;

import cz.developer.library.ui.image.ImageAdapter;
import cz.developer.library.ui.network.INetworkAdapter;
import cz.developer.library.ui.switchs.ISwitchInterface;

/**
 * Created by Administrator on 2016/11/7.
 */

public class DeveloperConfig {
    public ISwitchInterface switchConfig;
    public INetworkAdapter networkAdapter;
    public ImageAdapter imageAdapter;
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

        public Builder setNetworkAdapter(INetworkAdapter adapter){
            this.config.networkAdapter=adapter;
            return this;
        }

        public Builder setImageAdapter(ImageAdapter adapter){
            this.config.imageAdapter=adapter;
            return this;
        }

        public DeveloperConfig build(){
            return this.config;
        }
    }
}
