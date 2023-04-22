package nathan.csumb.vst;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class LandingPageActivity extends AppCompatActivity {


    private vstDAO mvstDAO;
    private SharedPreferences mSharedPreferences;

    private User mUser;

    public static Intent intentFactory(Context context) {

        return new Intent(context, LandingPageActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        TextView mWelcomeTextView = findViewById(R.id.welcomeTextView);
        TextView mWaterTextView = findViewById(R.id.waterTextView);
        Button mAdminAreaButton = findViewById(R.id.adminAreaButton);
        Button mSettingsButton = findViewById(R.id.settingsButton);
        Button mAddVitaminButton = findViewById(R.id.addVitaminButton);
        Button mTakeVitaminButton = findViewById(R.id.takeVitaminButton);
        Button mVitaminStackButton = findViewById(R.id.vitaminStackButton);
        Button mDrinkWaterButton = findViewById(R.id.drinkWater);
        Button mResetWaterButton = findViewById(R.id.resetWater);
        Button mLogoutButton = findViewById(R.id.logoutButton);

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
                        Water water = mvstDAO.getWater(userId);

                        int startQuantity = water.getGlassQuantity();
                        mWaterTextView.setText("Glasses: " + startQuantity + "💧");

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

        mTakeVitaminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date and format it as a string
                Date currentDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String dateString = formatter.format(currentDate);

                // Get the user id from shared preferences
                int userId = mSharedPreferences.getInt("userId", -1);

                // Check if the user has already clicked the button today
                String lastClickDate = mSharedPreferences.getString("lastClickDate_" + userId, "");
                if (dateString.equals(lastClickDate)) {
                    // The user has already clicked the button today, show a message or do nothing
                    Toast.makeText(LandingPageActivity.this, "You have already taken your vitamins today", Toast.LENGTH_SHORT).show();
                } else {
                    // The user has not clicked the button today, save the current date in shared preferences and do your action
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("lastClickDate_" + userId, dateString);
                    editor.apply();

                    VitaminQuantityDecrement decrement = new VitaminQuantityDecrement(LandingPageActivity.this);
                    decrement.start(userId);

                    Toast.makeText(LandingPageActivity.this, "Vitamin taken!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDrinkWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mvstDAO.getWater(userId) == null){
                            Water newWater = new Water(userId, 0);
                            mvstDAO.insert(newWater);
                        }

                        Water water = mvstDAO.getWater(userId);

                        int newQuantity = water.getGlassQuantity() + 1;

                        water.setGlassQuantity(newQuantity);
                        mvstDAO.update(water);

                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                mWaterTextView.setText("Glasses: " + newQuantity + "💧");
                            }
                        });
                    }
                }).start();
            }
        });

        mResetWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int userId = mSharedPreferences.getInt("userId", -1);

                        Water water = mvstDAO.getWater(userId);

                        water.setGlassQuantity(0);
                        mvstDAO.update(water);

                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                mWaterTextView.setText("Glasses: 0"+ "💧");
                            }
                        });
                    }
                }).start();
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

