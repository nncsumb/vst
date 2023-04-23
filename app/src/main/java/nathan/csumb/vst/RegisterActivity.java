package nathan.csumb.vst;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class RegisterActivity extends AppCompatActivity {

    private EditText mRUserNameEditText;
    private EditText mRPasswordEditText;

    private vstDAO mvstDAO;

    public static Intent intentFactory(Context context) {

        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeruser);

        mRUserNameEditText = findViewById(R.id.ruserNameEditText);
        mRPasswordEditText = findViewById(R.id.rpasswordEditText);

        getDatabase();

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mRUserNameEditText.getText().toString().trim();
                String password = mRPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    mRUserNameEditText.setError("Please enter a username.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mRPasswordEditText.setError("Please enter a password.");
                    return;
                }

                register(userName, password);
            }
        });

        Button cancelButton = findViewById(R.id.rcancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register(String userName, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User existingUser = mvstDAO.getUserByUsername(userName);
                if (existingUser != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                User newUser = new User(false, userName, password);
                mvstDAO.insert(newUser);
                int userId = mvstDAO.getUserByUsername(userName).getUserId();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "User registered successfully.", Toast.LENGTH_SHORT).show();
                    }
                });

                SharedPreferences mSharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putInt("userId", userId);
                editor.apply();

                Intent intent = LandingPageActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        }).start();
    }

    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getvstDAO();
    }
}

