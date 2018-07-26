package hu.robnn.commons.interfaces;

import java.util.Optional;

/**
 * MapContext for merging or mapping collections
 */
public interface MapContext {
    void addAdditionalData(Enum additionalDataKind, Object additionalData);
    Optional<Object> getAdditionalData(Enum additionalDataKind);
}
