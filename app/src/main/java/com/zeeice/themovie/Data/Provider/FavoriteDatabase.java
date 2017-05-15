package com.zeeice.themovie.Data.Provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Oriaje on 13/05/2017.
 */

@Database(version = FavoriteDatabase.VERSION)
public class FavoriteDatabase {

    private FavoriteDatabase() {
    }

    public static final int VERSION = 1;


    @Table(FavoriteColumns.class)
    public static final String FAVORITES = "favorites";

}
