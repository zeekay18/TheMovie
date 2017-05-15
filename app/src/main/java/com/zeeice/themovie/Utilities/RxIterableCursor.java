package com.zeeice.themovie.Utilities;

import android.database.Cursor;

import java.util.Iterator;

/**
 * Created by Oriaje on 13/05/2017.
 */

public class RxIterableCursor implements Iterable<Cursor> {

    private Cursor iterableCursor;

    public RxIterableCursor(Cursor c) {
        iterableCursor = c;
    }

    public static RxIterableCursor from(Cursor c) {
        return new RxIterableCursor(c);
    }


    @Override
    public Iterator<Cursor> iterator() {
        return RxCursorIterator.from(iterableCursor);
    }

    private static class RxCursorIterator implements Iterator<Cursor> {

        private final Cursor mCursor;

        public RxCursorIterator(Cursor cursor) {
            mCursor = cursor;
        }

        public static Iterator<Cursor> from(Cursor cursor) {
            return new RxCursorIterator(cursor);
        }

        @Override
        public boolean hasNext() {
            return !mCursor.isClosed() && mCursor.moveToNext();
        }

        @Override
        public Cursor next() {
            return mCursor;
        }
    }
}
