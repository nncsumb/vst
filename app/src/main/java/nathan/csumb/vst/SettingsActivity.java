package nathan.csumb.vst;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button profilePicButton = findViewById(R.id.s_profilePicButton);
        Button DeleteUserButton = findViewById(R.id.s_deleteButton);
        Button cancelButton = findViewById(R.id.s_cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPageActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });

        profilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        DeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserDeleteActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);

        return intent;
    }
}
