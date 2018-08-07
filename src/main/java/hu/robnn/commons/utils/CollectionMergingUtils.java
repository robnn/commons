package hu.robnn.commons.utils;

import hu.robnn.commons.exception.InvalidTargetCollectionException;
import hu.robnn.commons.interfaces.MapContext;
import hu.robnn.commons.interfaces.UuidHolder;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CollectionMergingUtils {

    private final static Logger LOGGER = Logger.getLogger("Mapper");

    /**
     * Merges the source collection with the target collection.
     * New items, modifications and deletions handled.
     * @param source the source collection to be merged
     * @param target the target collection, where the source will be merged
     * @param merger the merger method witch handles the mapping between the two types
     * @param context MapContext for handling additional data during the mapping procedure
     * @param <S> the type of the source entities
     * @param <T> the type of the target entities
     * @throws InvalidTargetCollectionException when the target collection is null
     * @return a new ArrayList with the merged entities
     */
    public static <S extends UuidHolder, T extends UuidHolder> Collection<T>
    mergeCollection(final Collection<S> source,
        Collection<T> target,
        final Merger<S, T> merger, final MapContext context) {

        final Map<String, T> beforeItems = new HashMap<>();

        if (target == null) {
            LOGGER.log(Level.SEVERE,
                "Target collection must not be null, please instantiate a new list");
           throw new InvalidTargetCollectionException();
        }
        if (source == null) {
            return target;
        }

        for (T oldItem : target) {
            final String key = oldItem.getUuid();
            beforeItems.put(key, oldItem);
        }

        final Map<String, T> afterItems = new HashMap<>();

        for (S sourceItem : source) {
            if (sourceItem != null) {
                final String key = sourceItem.getUuid();
                afterItems.put(key, merger.merge(sourceItem, beforeItems.get(key), context));
            }
        }

        getRemovedItems(beforeItems, afterItems).forEach(target::remove);
        target.addAll(getNewItems(beforeItems, afterItems));

        return target;
    }

    public interface Mapper<TS, TT> {

        TT map(TS source, MapContext context);

    }

    public interface Merger<TS, TT> {

        TT merge(TS source, TT target, MapContext context);
    }

    /**
     * Maps the entities in the given list, with the given mapper
     * @param sourceCollection the source collection to be mapped
     * @param mapper the mapper function, to handle the mapping per entity
     * @param context mapping context
     * @param <TS> the type of the source entities
     * @param <TT> the type of the target entities
     * @return a new ArrayList containing the mapped entities with type TT
     */
    public static <TS, TT> Collection<TT> mapCollection(final Collection<TS> sourceCollection,
        final Mapper<TS, TT> mapper,
        MapContext context) {
        Collection<TT> targetCollection = new ArrayList<>();
        if (sourceCollection == null || sourceCollection.isEmpty()) {
            return targetCollection;
        }
        targetCollection.addAll(sourceCollection.stream().map(ts -> mapper.map(ts, context))
            .collect(Collectors.toList()));
        return targetCollection;
    }

    private static <T> Collection<T> getNewItems(final Map<String, T> beforeItems,
        final Map<String, T> afterItems) {
        Collection<T> result  = new ArrayList<>();
        final Set<String> beforeKeys = beforeItems.keySet();
        for (Map.Entry<String, T> afterEntry : afterItems.entrySet()) {
            if (!beforeKeys.contains(afterEntry.getKey())) {
                result.add(afterEntry.getValue());
            }
        }
        return result;
    }

    private static <T> Collection<T> getRemovedItems(final Map<String, T> beforeItems,
        final Map<String, T> afterItems) {
        Collection<T> result  = new ArrayList<>();
        final Set<String> afterKeys = afterItems.keySet();
        for (Map.Entry<String, T> beforeEntry : beforeItems.entrySet()) {
            if (!afterKeys.contains(beforeEntry.getKey())) {
                result.add(beforeEntry.getValue());
            }
        }
        return result;
    }
}
