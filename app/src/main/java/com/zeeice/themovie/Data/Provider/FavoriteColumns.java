package com.zeeice.themovie.Data.Provider;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by Oriaje on 13/05/2017.
 */

public interface FavoriteColumns {

    @Unique(onConflict = ConflictResolutionType.REPLACE)
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    String OVERVIEW = "overview";

    @DataType(DataType.Type.REAL)
    String VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.TEXT)
    String BACK_DROP_PATH = "back_drop_path";

    @DataType(DataType.Type.TEXT)
    String POSTER_PATH = "poster_path";

    @DataType(DataType.Type.TEXT)
    String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.INTEGER)
    String IS_FAVORED = "is_favored";
}
