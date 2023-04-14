package nathan.csumb.vst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import nathan.csumb.vst.User;


@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "VST_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";

    public abstract vstDAO getvstDAO();

}
