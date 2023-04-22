package nathan.csumb.vst;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class MainActivity extends AppCompatActivity {
    private vstDAO mvstDAO;

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDatabase();
        try {
            if (mvstDAO.getUserByUsername("admin2") == null) {
                mvstDAO.insert(new User(true, "admin2", "admin2"));
            }
        } catch (Exception NullPointerException) {
            mvstDAO.insert(new User(true, "admin2", "admin2"));
        }
        try {
            if (mvstDAO.getUserByUsername("testuser1") == null) {
                mvstDAO.insert(new User(true, "testuser1", "testuser1"));
            }
        } catch (Exception NullPointerException) {
            mvstDAO.insert(new User(true, "testuser1", "testuser1"));
        }

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = LandingPageActivity.intentFactory(getApplicationContext());
            startActivity(intent);
            finish();
        }
        // Set text view for animations
        TextView vstTextView = findViewById(R.id.vstTextView);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        vstTextView.startAnimation(fadeInAnimation);
        // Set button click listeners and animations
        Button loginButton = findViewById(R.id.mloginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);
        loginButton.startAnimation(fadeInAnimation);
        createAccountButton.startAnimation(fadeInAnimation);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegisterActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
                .getvstDAO();
    }
}
