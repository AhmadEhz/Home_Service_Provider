package org.homeservice.entity;

public enum OrderStatus {
    WAITING_FOR_BID("waitingforbid"),
    WAITING_FOR_CHOOSE_SPECIALIST("waitingforchoosespecialist"),
    WAITING_FOR_COMING_SPECIALIST("waitingforcomingspecialist"),
    STARTED("started"),
    FINISHED("finished"),
    PAID("paid");

    private final String value;
    OrderStatus(String name) {
        this.value = name.toLowerCase();
    }
    public String getValue() {
        return value;
    }
}
