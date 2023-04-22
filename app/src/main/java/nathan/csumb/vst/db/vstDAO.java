package nathan.csumb.vst.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import nathan.csumb.vst.User;
import nathan.csumb.vst.Vitamin;

@Dao
public interface vstDAO {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(Vitamin... vitamins);

    @Update
    void update(Vitamin... vitamins);

    @Delete
    void delete(Vitamin... vitamins);

    @Query("SELECT * FROM " + AppDatabase.VITAMIN_TABLE + " WHERE vitaminId = :vitaminId ")
    Vitamin getVitaminsById(int vitaminId);

    @Query("SELECT * FROM " + AppDatabase.VITAMIN_TABLE + " WHERE userId = :userId  ORDER BY  quantity  ASC")
    List<Vitamin> getVitaminsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.VITAMIN_TABLE + " WHERE userId = :userId AND time = :timeString  ORDER BY  quantity  DESC")
    List<Vitamin> getVitaminsByUserIdByQtyDesc(int userId, String timeString);

    @Query("SELECT * FROM " + AppDatabase.VITAMIN_TABLE + " WHERE userId = :userId AND time = :timeString  ORDER BY  quantity  ASC")
    List<Vitamin> getVitaminsByUserIdByQtyAsc(int userId, String timeString);

    @Query("SELECT * FROM " + AppDatabase.VITAMIN_TABLE + " WHERE userId = :userId AND time = :timeString  ORDER BY  name  DESC")
    List<Vitamin> getVitaminsByUserIdByNameDesc(int userId, String timeString);

    @Query("SELECT * FROM " + AppDatabase.VITAMIN_TABLE + " WHERE userId = :userId AND time = :timeString  ORDER BY  name  ASC")
    List<Vitamin> getVitaminsByUserIdByNameAsc(int userId, String timeString);

    @Query("SELECT * FROM " + AppDatabase.VITAMIN_TABLE + " WHERE userId = :userId AND time = :timeString AND name = :nameString")
    Vitamin getVitaminsByName(int userId, String timeString, String nameString);

    @Query("DELETE FROM " + AppDatabase.USER_TABLE)
    void deleteAllUsers();

    @Query("DELETE FROM " + AppDatabase.VITAMIN_TABLE)
    void deleteAllVitamins();


}
