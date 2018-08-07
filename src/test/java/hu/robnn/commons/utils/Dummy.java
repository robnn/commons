package hu.robnn.commons.utils;

import hu.robnn.commons.interfaces.UuidHolder;

public class Dummy implements UuidHolder {

    private String uuid;
    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
