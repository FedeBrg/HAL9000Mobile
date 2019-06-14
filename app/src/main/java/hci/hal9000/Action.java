package hci.hal9000;

import java.util.ArrayList;

public class Action<T> {
    String deviceId;
    String actionName;
    ArrayList<T> params;
    String meta;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public ArrayList<T> getParams() {
        return params;
    }

    public void setParams(ArrayList<T> params) {
        this.params = params;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
