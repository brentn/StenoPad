package com.brentandjody.stenopad.Translation.tests;

import android.test.AndroidTestCase;

import com.brentandjody.stenopad.Display.DisplayDevice;
import com.brentandjody.stenopad.Display.DisplayItem;
import com.brentandjody.stenopad.Translation.Dictionary;
import com.brentandjody.stenopad.Translation.Stroke;
import com.brentandjody.stenopad.Translation.Translator;

import java.util.concurrent.CountDownLatch;


public class TranslatorTest extends AndroidTestCase implements DisplayDevice.Display {

    private Dictionary dictionary;
    private Translator translator;
    private boolean loaded = false;
    private int backspaces = 0;
    private String text = "";
    private String preview = "";

    public void setUp() throws Exception {
        super.setUp();
        if (! loaded ) {
            final CountDownLatch latch = new CountDownLatch(1);
            dictionary = new Dictionary(getContext());
            dictionary.load("test.json");
            dictionary.setOnDictionaryLoadedListener(new Dictionary.OnDictionaryLoadedListener() {
                @Override
                public void onDictionaryLoaded() {
                    latch.countDown();
                }
            });
            latch.await();
            loaded = true;
        }
        translator = new Translator(dictionary);
    }

    public void update(DisplayItem item, String preview) {
        backspaces = item.getBackspaces();
        text = item.getText();
    }

    public void testIsLoaded() throws Exception {
        assertTrue(loaded);
    }

    public void testTranslate() throws Exception {
        assertEquals(0, backspaces);
        assertEquals("", text);
        assertEquals("", preview);
        translator.translate(new Stroke("*"), this);
        translator.translate(new Stroke("AD"), this);
        translator.translate(new Stroke("SKWRAEUS"), this);
        translator.translate(new Stroke("EPBT"), this);
        translator.translate(new Stroke("SKWRAOUR"), this);
        //TODO: more testing
    }
}