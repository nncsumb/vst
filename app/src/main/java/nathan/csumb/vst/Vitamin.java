package nathan.csumb.vst;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nathan.csumb.vst.db.AppDatabase;

@Entity(tableName = AppDatabase.VITAMIN_TABLE)
public class Vitamin {

    @PrimaryKey(autoGenerate = true)
    private int vitaminId;
    private int userId;
    private String name;
    private String description;
    private String time;
    private int quantity;
//    public byte[] vitamin_picture;

    public Vitamin(int userId, String name, String description, String time, int quantity) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.time = time;
        this.quantity = quantity;
//        this.vitamin_picture = vitamin_picture;
    }

    public int getVitaminId() {
        return vitaminId;
    }

    public void setVitaminId(int vitaminId) {
        this.vitaminId = vitaminId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

//    public byte[] getVitamin_picture() {
//        return vitamin_picture;
//    }
//
//    public void setVitamin_picture(byte[] vitamin_picture) {
//        this.vitamin_picture = vitamin_picture;
//    }

    @Override
    public String toString() {
        return "Vitamin{" +
                "  mName='" + name + '\'' +
                ", mDescription='" + description + '\'' +
                ", quantity=" + quantity +
                ", time=" + time +
//                ", vitamin_picture=" + Arrays.toString(vitamin_picture) +
                '}';
    }

}
