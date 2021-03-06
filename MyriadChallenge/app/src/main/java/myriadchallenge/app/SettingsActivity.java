package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SettingsActivity extends Activity {

    Button saveButton, cancelButton;

    String displayName, locationOfOrigin, alignment;

    Intent intent;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);

        user = ParseUser.getCurrentUser();

        intent = new Intent(SettingsActivity.this, QuestListActivity.class);

        displayName = user.getString("userDisplayName");
        locationOfOrigin = user.getString("userLocationOfOrigin");
        alignment = user.getString("userAlignment");

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

        try{
            // If name was entered, update displayName
            if( !(enteredDisplayName.getText().toString().equals("")) ){
                displayName = enteredDisplayName.getText().toString();
                user.put("userDisplayName",displayName);
            }
        }
        catch (NullPointerException e){
            // Something went wrong, so displayName
            // stays the same as it was read from Parse.
        }

        try{
            // If location was entered, update locationOfOrigin
            if( !(enteredLocationOfOrigin.getText().toString().equals("")) ){
                locationOfOrigin = enteredLocationOfOrigin.getText().toString();
                user.put("userLocationOfOrigin",locationOfOrigin);
            }
        }
        catch (NullPointerException e){
            // Something went wrong, so locationOfOrigin
            // stays the same as it was read from Parse.
        }

        try{
            // If alignment was changed, update alignment
            if( !(alignmentSpinner.getSelectedItem().toString().equals(alignment)) ){
                alignment = alignmentSpinner.getSelectedItem().toString();
                user.put("userAlignment",alignment);
            }
        }
        catch (NullPointerException e){
            // Something went wrong, so alignment
            // stays the same as it was read from Parse.
        }

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                returnToQuestListActivity(e);
            }
        });

    }

    private void returnToQuestListActivity(ParseException e) {
        if(e == null){
            startActivity(intent);
            finish();
        }
    }

    public void cancelButtonPressed(){
        finish();
    }
}
