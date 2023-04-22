package nathan.csumb.vst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import nathan.csumb.vst.User;
import nathan.csumb.vst.Vitamin;


@Database(entities = {Vitamin.class, User.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "VST_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String VITAMIN_TABLE = "VITAMIN_TABLE";

    public abstract vstDAO getvstDAO();

}
