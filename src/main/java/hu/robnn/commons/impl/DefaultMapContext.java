package hu.robnn.commons.impl;

import hu.robnn.commons.interfaces.MapContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultMapContext implements MapContext {
    private final Map<Enum, Object> context = new HashMap<>();


    @Override
    public void addAdditionalData(Enum additionalDataKind, Object additionalData) {
        context.put(additionalDataKind,additionalData);
    }

    @Override
    public Optional<Object> getAdditionalData(Enum additionalDataKind) {
        Object additionalData = context.get(additionalDataKind);
        return Optional.ofNullable(additionalData);
    }
}
