package nathan.csumb.vst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class DatabaseTest {

    private AppDatabase mDatabase;
    private vstDAO mvstDao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mvstDao = mDatabase.getvstDAO();
        mvstDao.deleteAllUsers();
        mvstDao.deleteAllVitamins();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void insertAndGetUser() throws Exception {
        User user = new User(false,"testUser", "testPassword");
        mvstDao.insert(user);
        User retrievedUser = mvstDao.getUserByUsername("testUser");
        assertEquals(user.getUserName(), retrievedUser.getUserName());
    }

    @Test
    public void updateAndGetUser() throws Exception {
        User user = new User(false,"testUser", "testPassword");
        mvstDao.insert(user);
        User userToUpdate = mvstDao.getUserByUsername("testUser");
        userToUpdate.setPassword("newPassword");
        mvstDao.update(userToUpdate);
        User retrievedUser = mvstDao.getUserByUsername("testUser");
        assertEquals(userToUpdate.getPassword(), retrievedUser.getPassword());
    }

    @Test
    public void deleteAndGetUser() throws Exception {
        User user = new User(false,"testUser", "testPassword");
        mvstDao.insert(user);
        mvstDao.delete(user);
        User retrievedUser = mvstDao.getUserByUserId(user.getUserId());
        assertNull(retrievedUser);
    }

    @Test
    public void insertAndGetVitamin() throws Exception {
        Vitamin vitamin = new Vitamin(1,"testVitamin", "test", "Morning", 1);
        mvstDao.insert(vitamin);
        Vitamin retrievedVitamin = mvstDao.getVitaminsById(1);
        assertEquals(vitamin.getName(), retrievedVitamin.getName());
    }

    @Test
    public void updateAndGetVitamin() throws Exception {
        Vitamin vitamin = new Vitamin(1,"testVitamin", "test", "Morning", 1);
        mvstDao.insert(vitamin);
        Vitamin vitaminToUpdate = mvstDao.getVitaminsById(1);
        vitaminToUpdate.setQuantity(50);
        mvstDao.update(vitaminToUpdate);
        Vitamin retrievedVitamin = mvstDao.getVitaminsById(1);
        assertEquals(vitaminToUpdate.getQuantity(), retrievedVitamin.getQuantity());
    }

    @Test
    public void deleteAndGetVitamin() throws Exception {
        Vitamin vitamin = new Vitamin(1,"testVitamin", "test", "Morning", 1);
        mvstDao.insert(vitamin);
        mvstDao.delete(vitamin);
        Vitamin retrievedVitamin = mvstDao.getVitaminsById(vitamin.getVitaminId());
        assertNull(retrievedVitamin);
    }
}



