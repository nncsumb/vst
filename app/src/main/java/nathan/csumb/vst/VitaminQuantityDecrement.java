package nathan.csumb.vst;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class VitaminQuantityDecrement {

    private static vstDAO mvstDAO;
    private final Context mContext;

    public VitaminQuantityDecrement(Context context) {
        mContext = context;
    }

    public void start(int userId) {
        getDatabase();

        // Get vitamins that were updated on previous day
        List<Vitamin> vitamins = mvstDAO.getVitaminsByUserId(userId);

        // Reduce quantity for each vitamin by 1
        for (Vitamin vitamin : vitamins) {
            int currentQuantity = vitamin.getQuantity();
            if (currentQuantity > 0) {
                vitamin.setQuantity(currentQuantity - 1);
                mvstDAO.update(vitamin);
            }
        }
    }

    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(mContext, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getvstDAO();
    }
}
