package nathan.csumb.vst.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import nathan.csumb.vst.User;
import nathan.csumb.vst.Vitamin;
import nathan.csumb.vst.Water;


@Database(entities = {Water.class, Vitamin.class, User.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "VST_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String VITAMIN_TABLE = "VITAMIN_TABLE";
    public static final String WATER_TABLE = "WATER_TABLE";

    public abstract vstDAO getvstDAO();

}
