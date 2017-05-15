package com.zeeice.themovie.ui.Listner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Oriaje on 3/8/2017.
 */
public class MyRecyclerItemClickListener implements
        RecyclerView.OnItemTouchListener{

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public MyRecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(
                context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        }
        );
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {

        View child = recyclerView.findChildViewUnder(e.getX(),
                e.getY());
        if (child != null && mListener != null &mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(child, recyclerView.
                    getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
