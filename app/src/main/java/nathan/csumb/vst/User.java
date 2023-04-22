package nathan.csumb.vst;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nathan.csumb.vst.db.AppDatabase;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

    private final boolean admin;
    @PrimaryKey(autoGenerate = true)
    private int mUserId;
    private String mUserName;
    private String mPassword;

    public User(boolean admin, String userName, String password) {
        this.admin = admin;
        mUserName = userName;
        mPassword = password;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" + "mUserId=" + mUserId + ", mUserName='" + mUserName + '\'' + ", mPassword='" + mPassword + '\'' + '}';
    }

    public boolean isAdmin() {
        return this.admin;
    }
}
