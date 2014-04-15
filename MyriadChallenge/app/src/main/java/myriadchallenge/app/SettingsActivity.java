package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

    Button saveButton, cancelButton;

    String displayName, locationOfOrigin, alignment;

    Intent receiveIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        receiveIntent = getIntent();

        displayName = receiveIntent.getExtras().getString("Display Name");
        locationOfOrigin = receiveIntent.getExtras().getString("Location of Origin");
        alignment = receiveIntent.getExtras().getString("Alignment");

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
        EditText enteredLocationOfOrigin = (EditText)findViewById(R.id.location_of_origin);
        Spinner alignmentSpinner = (Spinner)findViewById(R.id.alignment_spinner);

        Intent intent = new Intent(SettingsActivity.this, QuestListActivity.class);

        try{
            // If name was entered, update displayName
            if( !(enteredDisplayName.getText().toString().equals("")) ){
                displayName = enteredDisplayName.getText().toString();
            }
        }
        catch (NullPointerException e){
            // No name entered
        }
        intent.putExtra("Display Name",displayName);

        try{
            // If location was entered, update locationOfOrigin
            if( !(enteredLocationOfOrigin.getText().toString().equals("")) ){
                locationOfOrigin = enteredLocationOfOrigin.getText().toString();
            }
        }
        catch (NullPointerException e){
            // No location entered
        }
        intent.putExtra("Location of Origin",locationOfOrigin);

        try{
            // If alignment was changed, update alignment
            if( !(alignmentSpinner.getSelectedItem().toString().equals(alignment)) ){
                alignment = alignmentSpinner.getSelectedItem().toString();
            }
        }
        catch (NullPointerException e){
            // No alignment change
        }
        intent.putExtra("Alignment",alignment);

        startActivity(intent);
    }

    public void cancelButtonPressed(){

        super.onBackPressed();
    }
}
