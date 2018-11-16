package restClient;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "custom")
public class RestHttpProperties {
    private String settingName;

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(final String value) {
        this.settingName = value;
    }
}