package nathan.csumb.vst;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class AddVitaminActivity extends AppCompatActivity {

    private vstDAO mvstDAO;
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private Spinner mTimeSpinner;
    private EditText mQuantityEditText;

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, AddVitaminActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vitamin);

        getDatabase();

        mNameEditText = findViewById(R.id.nameEditText);
        mDescriptionEditText = findViewById(R.id.descriptionEditText);
        mTimeSpinner = findViewById(R.id.timeSpinner);
        mQuantityEditText = findViewById(R.id.quantityEditText);
        SharedPreferences mSharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        int userId = mSharedPreferences.getInt("userId", -1);

        Button addButton = findViewById(R.id.submitButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                String time = mTimeSpinner.getSelectedItem().toString();
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(mQuantityEditText.getText().toString());
                } catch (Exception NumberFormatException) {
                    quantity = 0;
                }


                if (TextUtils.isEmpty(name)) {
                    mNameEditText.setError("Name is required");
                    return;
                }

                if (quantity == 0) {
                    mQuantityEditText.setError("Quantity is required");
                    return;
                }
                Vitamin existingVitamin = mvstDAO.getVitaminsByName(userId, time, name);
                if (existingVitamin != null) {
                    // A vitamin with the same name already exists, prompt the user to enter a unique name
                    Toast.makeText(AddVitaminActivity.this, "A vitamin with the same name already exists. Please enter a unique name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Vitamin vitamin = new Vitamin(userId, name, description, time, quantity);
                mvstDAO.insert(vitamin);

                Toast.makeText(AddVitaminActivity.this, "Vitamin added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPageActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getvstDAO();
    }
}

