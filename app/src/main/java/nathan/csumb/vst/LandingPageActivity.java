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
    private Button mSettingsButton;
    private Button mAddVitaminButton;
    private Button mVitaminStackButton;
    private Button mAdminAreaButton;
    private Button mLogoutButton;
    private QuantityNotification mQuantityNotification;

    private vstDAO mvstDAO;
    private SharedPreferences mSharedPreferences;

    private User mUser;

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LandingPageActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mQuantityNotification = new QuantityNotification(this);
        mQuantityNotification.start();
        mWelcomeTextView = findViewById(R.id.welcomeTextView);
        mAdminAreaButton = findViewById(R.id.adminAreaButton);
        mSettingsButton = findViewById(R.id.settingsButton);
        mAddVitaminButton = findViewById(R.id.addVitaminButton);
        mVitaminStackButton = findViewById(R.id.vitaminStackButton);
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
                            mAdminAreaButton.setVisibility(View.VISIBLE);
                        } else {
                            mAdminAreaButton.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();

        mVitaminStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the existing fragment, if any, with the VitaminListFragment
                VitaminListFragment vitaminListFragment = new VitaminListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, vitaminListFragment).addToBackStack(null).commit();
            }
        });
        mAddVitaminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddVitaminActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });
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
        mvstDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getvstDAO();
    }
}

