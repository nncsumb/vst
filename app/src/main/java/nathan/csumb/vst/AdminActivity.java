package nathan.csumb.vst;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class AdminActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private vstDAO mvstDAO;

    public static Intent intentFactory(Context context) {

        return new Intent(context, AdminActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_controls);

        mUsernameEditText = findViewById(R.id.a_userNameEditText);
        Button mConfirmButton = findViewById(R.id.a_confirmButton);

        getDatabase();

        Button cancelButton = findViewById(R.id.a_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPageActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    mUsernameEditText.setError("Please enter a username");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = mvstDAO.getUserByUsername(username);

                        if (user == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUsernameEditText.setError("User not found");
                                }
                            });
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                                builder.setTitle("Confirm Deletion").setMessage("Are you sure you want to delete " + username + "'s account?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mvstDAO.delete(user);

                                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                                        int userId = sharedPreferences.getInt("userId", -1);

                                        if (user.getUserId() == userId) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean("isLoggedIn", false);
                                            editor.putInt("userId", -1);
                                            editor.apply();

                                            Intent intent = MainActivity.intentFactory(getApplicationContext());
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "User account deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getvstDAO();
    }
}

