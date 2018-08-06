package hu.robnn.commons.utils;

import hu.robnn.commons.interfaces.UuidHolder;

import java.awt.*;

import static org.junit.Assert.*;

public class CollectionMergingUtilsTest {

    class Dummy implements UuidHolder{


        private String uuid;

        public Dummy(String uuid){
            this.uuid=uuid;
        }

        @Override
        public String getUuid() {
            return this.uuid;
        }

        @Override
        public void setUuid(String uuid) {
            this.uuid=uuid;
        }
    }

    @org.junit.Test
    public void mergeCollection() {
        Dummy dummy1 = new Dummy();

    }

    @org.junit.Test
    public void mapCollection() {



    }
}