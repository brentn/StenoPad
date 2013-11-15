package com.brentandjody.stenopad;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by brent on 14/11/13.
 * takes a stream of steno strokes, and produces a stream of English text
 */
public class Translator {

    private static final int HISTORY_SIZE = 50;

    private StenoDictionary dictionary;
    private Deque<Stroke> strokeQ;
    private LimitedSizeDeque<Translation> history;

    public Translator(StenoDictionary d) {
        dictionary = d;
        strokeQ = new ArrayDeque<Stroke>();
        history = new LimitedSizeDeque<Translation>(HISTORY_SIZE);
    }

    private void translate(Stroke stroke) {
        List<Translation> play = new LinkedList<Translation>();
        List<Translation> undo = new LinkedList<Translation>();
        Translation translation;
        List<Stroke> strokes;
        String lookup;
        // process a single stroke
        if (stroke.isCorrection()) {
            if (! strokeQ.isEmpty()) {
                undoFromQueue();
            } else {
                undoFromHistory();
            }
        } else {
            if (strokeQ.isEmpty()) {
                lookup = dictionary.lookup(stroke.rtfcre());
                if (lookup==null) { //not found
                    translation = new Translation(stroke.asArray(), stroke.rtfcre());
                } else {
                    if (lookup.isEmpty()) { //ambiguous
                        translation = null;
                        strokeQ.add(stroke);
                    } else { //deterministic
                        translation = new Translation(stroke.asArray(), lookup);
                    }
                }
            } else {
                lookup = dictionary.lookup(Stroke.combine(strokesInQueue()) + "/" + stroke);
                if (lookup==null) {
                    translation = new Translation(strokesInQueue(), dictionary.lookup(Stroke.combine(strokesInQueue())));
                    strokeQ.add(stroke);
                } else {
                    if (lookup.isEmpty()) { //ambiguous
                        translation = null;
                        strokeQ.add(stroke);
                    } else { //deterministic
                        strokeQ.add(stroke);
                        translation = new Translation(strokesInQueue(), lookup);
                        strokeQ.clear();
                    }
                }
            }
        }
    }

    private void undoFromQueue() {
        //TODO:
    }

    private void undoFromHistory() {
        //TODO:
    }

    private Stroke[] strokesInQueue() {
        Stroke[] result = new Stroke[strokeQ.size()];
        return strokeQ.toArray(result);
    }

    class LimitedSizeDeque<Value> extends LinkedBlockingDeque<Value> {
        private int size_limit;

        public LimitedSizeDeque(int size) {
            size_limit = size;
        }

        @Override
        public void addFirst(Value value) {
            System.err.print("Cannot add history at this end of the deque");
        }

        @Override
        public void addLast(Value value) {
            add(value);
        }

        @Override
        public boolean add(Value value) {
            boolean result = super.add(value); //addLast
            while (size() > size_limit) {
                removeFirst();
            }
            return result;
        }
    }
}