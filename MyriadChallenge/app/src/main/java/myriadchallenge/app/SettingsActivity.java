package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {

    private Button saveButton, cancelButton;

    final int BUTTON_PRESSED = 0xFF;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);

        saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(view == saveButton){
                    saveButtonPressed();
                }
            }
        });

        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(view == cancelButton){
                    cancelButtonPressed();
                }
            }
        });
    }

    public void saveButtonPressed(){

        EditText enteredDisplayName = (EditText)findViewById(R.id.display_name);
        String displayName = enteredDisplayName.getText().toString();

        EditText enteredLocationOfOrigin = (EditText)findViewById(R.id.location_of_origin);
        String locationOfOrigin = enteredLocationOfOrigin.getText().toString();

        Intent intent = new Intent(SettingsActivity.this, QuestListActivity.class);
        intent.putExtra("Display Name",displayName);
        intent.putExtra("Location of Origin",locationOfOrigin);
        startActivity(intent);
    }

    public void cancelButtonPressed(){

        super.onBackPressed();
    }
}
