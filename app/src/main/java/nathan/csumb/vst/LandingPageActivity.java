package nathan.csumb.vst;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class LandingPageActivity extends AppCompatActivity {

    private TextView mWelcomeTextView;
    private TextView mAdminTextView;
    private Button mSettingsButton;
    private Button mAdminAreaButton;
    private Button mLogoutButton;

    private vstDAO mvstDAO;
    private SharedPreferences mSharedPreferences;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mWelcomeTextView = findViewById(R.id.welcomeTextView);
        mAdminTextView = findViewById(R.id.adminTextView);
        mAdminAreaButton = findViewById(R.id.adminAreaButton);
        mSettingsButton = findViewById(R.id.settingsButton);
        mLogoutButton = findViewById(R.id.logoutButton);

        getDatabase();
        mSharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        int userId = mSharedPreferences.getInt("userId", -1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mUser = mvstDAO.getUserByUserId(userId);

                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        mWelcomeTextView.setText("Welcome, " + mUser.getUserName() + "!");

                        if (mUser.isAdmin()) {
                            mAdminTextView.setVisibility(View.VISIBLE);
                            mAdminAreaButton.setVisibility(View.VISIBLE);
                        } else {
                            mAdminTextView.setVisibility(View.GONE);
                            mAdminAreaButton.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SettingsActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });
        mAdminAreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.putInt("userId", -1);
                editor.apply();

                Intent intent = MainActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });
    }
    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getvstDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LandingPageActivity.class);

        return intent;
    }
}

