package hu.robnn.commons.interfaces;

/**
 * UuidHolder for marking a class that it can be handled by the merger
 */
public interface UuidHolder {
    String getUuid();
    void setUuid(String uuid);
}
