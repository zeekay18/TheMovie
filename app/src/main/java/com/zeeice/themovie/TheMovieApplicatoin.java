package com.zeeice.themovie;

import android.app.Application;
import android.content.Context;

/**
 * Created by Oriaje on 26/04/2017.
 */

public class TheMovieApplicatoin extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static TheMovieApplicatoin getApplication(Context context) {
        return (TheMovieApplicatoin) context.getApplicationContext();
    }
}
