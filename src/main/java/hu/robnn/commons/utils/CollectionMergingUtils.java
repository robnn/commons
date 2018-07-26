package hu.robnn.commons.utils;

import hu.robnn.commons.interfaces.MapContext;
import hu.robnn.commons.interfaces.UuidHolder;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CollectionMergingUtils {
    private final static Logger LOGGER = Logger.getLogger("Mapper");

    public static <T extends UuidHolder, S extends UuidHolder> Collection<S>
    mergeCollection(final Collection<T> source,
                    Collection<S> target,
                    final Merger<T, S> merger, final MapContext context, final Class<? extends Collection<S>> targetCollectionClass) {

        final Map<String, S> beforeItems = new HashMap<>();

        if (target == null) {
            try {
                target = targetCollectionClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Failed to instantiate target collection");
                throw new RuntimeException(e);
            }
        }
        if (source == null) {
            return target;
        }

        for (S oldItem : target) {
            final String key = oldItem.getUuid();
            beforeItems.put(key, oldItem);
        }

        final Map<String, S> afterItems = new HashMap<>();

        for (T sourceItem : source) {
            if (sourceItem != null) {
                final String key = sourceItem.getUuid();
                afterItems.put(key, merger.merge(sourceItem, beforeItems.get(key), context));
            }
        }

        getRemovedItems(beforeItems, afterItems, targetCollectionClass).forEach(target::remove);
        target.addAll(getNewItems(beforeItems, afterItems, targetCollectionClass));

        return target;
    }

    public interface Mapper<TS, TT> {

        TT map(TS source, MapContext context);

    }

    public interface Merger<TS, TT> {

        TT merge(TS source, TT target, MapContext context);
    }

    public <TS, TT> Collection<TT> mapCollection(final Collection<TS> sourceCollection, final Mapper<TS, TT> mapper,
                                   MapContext context,
    final Class<? extends Collection<TT>> targetCollectionClass) {
        Collection<TT> targetCollection;
        try {
            targetCollection = targetCollectionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Failed to instantiate target collection");
            throw new RuntimeException(e);
        }
        if (sourceCollection == null || sourceCollection.isEmpty()) {
            return targetCollection;
        }
        targetCollection.addAll(sourceCollection.stream().map(ts -> mapper.map(ts, context))
                .collect(Collectors.toList()));
        return targetCollection;
    }

    private static <T> Collection<T> getNewItems(final Map<String, T> beforeItems,
                                                 final Map<String, T> afterItems,
                                                 final Class<? extends Collection<T>> targetCollectionClass) {
        Collection<T> result;
        try {
            result = targetCollectionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Failed to instantiate target collection");
            throw new RuntimeException(e);
        }
        final Set<String> beforeKeys = beforeItems.keySet();
        for (Map.Entry<String, T> afterEntry : afterItems.entrySet()) {
            if (!beforeKeys.contains(afterEntry.getKey())) {
                result.add(afterEntry.getValue());
            }
        }
        return result;
    }

    private static <T> Collection<T> getRemovedItems(final Map<String, T> beforeItems,
                                                     final Map<String, T> afterItems,
                                                     final Class<? extends Collection<T>> targetCollectionClass) {
        Collection<T> result;
        try {
            result = targetCollectionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Failed to instantiate target collection");
            throw new RuntimeException(e);
        }
        final Set<String> afterKeys = afterItems.keySet();
        for (Map.Entry<String, T> beforeEntry : beforeItems.entrySet()) {
            if (!afterKeys.contains(beforeEntry.getKey())) {
                result.add(beforeEntry.getValue());
            }
        }
        return result;
    }
}
