package nathan.csumb.vst;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nathan.csumb.vst.db.AppDatabase;

@Entity(tableName = AppDatabase.WATER_TABLE)
public class Water {

    @PrimaryKey(autoGenerate = true)
    private int waterId;
    private int userId;
    private int glassQuantity;
    public Water(int userId, int glassQuantity) {
        this.userId = userId;
        this.glassQuantity = glassQuantity;
    }

    public int getWaterId() {
        return waterId;
    }

    public void setWaterId(int waterId) {
        this.waterId = waterId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGlassQuantity() {
        return glassQuantity;
    }

    public void setGlassQuantity(int glassQuantity) {
        this.glassQuantity = glassQuantity;
    }
}
