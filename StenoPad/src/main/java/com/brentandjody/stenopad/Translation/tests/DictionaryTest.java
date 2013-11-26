package com.brentandjody.stenopad.Translation.tests;

import android.test.AndroidTestCase;

import com.brentandjody.stenopad.Translation.Dictionary;
import com.brentandjody.stenopad.Translation.Stroke;

import junit.framework.Assert;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


public class DictionaryTest extends AndroidTestCase {


    public void testIsLoading() throws Exception {
        Dictionary dictionary = new Dictionary(getContext());
        final CountDownLatch latch = new CountDownLatch(1);
        assertFalse(dictionary.isLoading());
        dictionary.load("dict.json");
        assertTrue(dictionary.isLoading());
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch.countDown();
            }
        });
        latch.await();
        assertFalse(dictionary.isLoading());
    }

    public void testBadFilename() throws Exception {
        Dictionary dictionary = new Dictionary(getContext());
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        dictionary.load("booga.json");
        assertEquals("Dictionary File: booga.json could not be found", errContent.toString().trim());
    }

    public void testIllegalFileType() throws Exception{
        Dictionary dictionary = new Dictionary(getContext());
        try {
            dictionary.load("test.rtf");
            Assert.fail("Illegal file type");
        } catch (Exception e) {
        }
    }

    public void testLoadAndClear() throws Exception {
        Dictionary dictionary = new Dictionary(getContext());
        final CountDownLatch latch = new CountDownLatch(1);
        assertEquals(0, dictionary.size());
        dictionary.load("test.json");
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch.countDown();
            }
        });
        latch.await();
        int size = dictionary.size();
        assertTrue(size > 0);
        dictionary.clear();
        assertEquals(0, dictionary.size());
    }

    public void testOverrideEntries() throws Exception{
        Dictionary dictionary = new Dictionary(getContext());
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch1 = new CountDownLatch(1);
        assertEquals(0,dictionary.size());
        dictionary.load("test.json");
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch.countDown();
            }
        });
        latch.await();
        final int size = dictionary.size();
        assertTrue(size > 0);
        assertEquals("adjudicator", dictionary.lookup("AD/SKWRAOUD/KAEUT/TOR"));
        dictionary.load(("test2.json"));
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch1.countDown();
            }
        });
        latch1.await();
        assertEquals(size, dictionary.size());
        assertEquals("judge", dictionary.lookup("AD/SKWRAOUD/KAEUT/TOR"));
    }

    public void testLoad2Dictionaries() throws Exception{
        Dictionary dictionary = new Dictionary(getContext());
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch1 = new CountDownLatch(1);
        assertEquals(0,dictionary.size());
        dictionary.load("test.json");
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch.countDown();
            }
        });
        latch.await();
        int size = dictionary.size();
        assertTrue(size > 0);
        dictionary.load(("test3.json"));
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch1.countDown();
            }
        });
        latch1.await();
        assertTrue(dictionary.size() > size);
    }

    public void testLookupAndForceLookup() throws Exception {
        Dictionary dictionary = new Dictionary(getContext());
        final CountDownLatch latch = new CountDownLatch(1);
        dictionary.load("test.json");
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch.countDown();
            }
        });
        latch.await();
        assertNull(dictionary.lookup("AD/R-R"));
        assertNull(dictionary.forceLookup("AD/R-R"));
        assertEquals("adjudication", dictionary.lookup("AD/SKWRAOUD/KAEUGS"));
        assertEquals("adjudication", dictionary.forceLookup("AD/SKWRAOUD/KAEUGS"));
        assertEquals("", dictionary.lookup("AD/SKWRAOUD/KAEUT"));
        assertEquals("adjudicate", dictionary.forceLookup("AD/SKWRAOUD/KAEUT"));
    }

    public void testLongestValidStroke() throws Exception {
        Dictionary dictionary = new Dictionary(getContext());
        final CountDownLatch latch = new CountDownLatch(1);
        dictionary.load("test.json");
        dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
            @Override
            public void onDictionaryLoaded() {
                latch.countDown();
            }
        });
        latch.await();
        assertTrue(Arrays.equals(Stroke.separate("AD/SREPB/KHUR"), dictionary.longestValidStroke("AD/SREPB/KHUR/OU")));
    }


}
