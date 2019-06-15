package hci.hal9000;

import java.util.Map;

public class Event {
    String deviceId;
    String event;

    public Event(String deviceId, String event){
        this.deviceId = deviceId;
        this.event = event;
    }
}
