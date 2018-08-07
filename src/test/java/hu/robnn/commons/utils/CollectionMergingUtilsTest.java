package hu.robnn.commons.utils;

import static org.junit.Assert.*;

import hu.robnn.commons.impl.DefaultMapContext;
import java.util.ArrayList;

public class CollectionMergingUtilsTest {

    @org.junit.Test
    public void mergeCollection() {
        //dummy hívás, hasonló képpen kell meghívni egy rendes collectionre, majd ellenőrizni, hogy pl törlődött e egy listából valami
        CollectionMergingUtils.mergeCollection(new ArrayList<Dummy>(), new ArrayList<Dummy>(), (x,y,z)-> x, new DefaultMapContext());
    }

    @org.junit.Test
    public void mapCollection() {
    }
}