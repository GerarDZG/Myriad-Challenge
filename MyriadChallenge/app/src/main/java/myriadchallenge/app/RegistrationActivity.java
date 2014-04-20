package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.Vector;

public class RegistrationActivity extends Activity{

    String registrationUserNameString, registrationPasswordString, registrationRetypePasswordString,
        registrationDisplayNameString, registrationAlignmentString, registrationLocationOfOriginString;

    EditText registrationUserName, registrationPassword, registrationRetypePassword,
        registrationDisplayName, registrationLocationOfOrigin;

    Spinner registrationAlignmentSpinner;

    Button registrationButton;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        Intent intent = getIntent();

        user = new ParseUser();

        registrationUserName = (EditText)findViewById(R.id.registration_username);
        registrationPassword = (EditText)findViewById(R.id.registration_password);
        registrationRetypePassword = (EditText)findViewById(R.id.registration_retype_password);
        registrationDisplayName = (EditText)findViewById(R.id.registration_display_name);
        registrationLocationOfOrigin = (EditText)findViewById(R.id.registration_location_of_origin);

        registrationAlignmentSpinner = (Spinner)findViewById(R.id.registration_alignment_spinner);

        registrationButton = (Button)findViewById(R.id.registration_button);

        registrationUserNameString = intent.getExtras().getString("User Name");
        registrationPasswordString = intent.getExtras().getString("Password");

        registrationUserName.setText(registrationUserNameString);
        registrationPassword.setText(registrationPasswordString);


        registrationButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(view == registrationButton){
                    registrationButtonPressed();
                }
            }
        });
    }

    public void registrationButtonPressed(){

        try{
            registrationUserNameString = registrationUserName.getText().toString();
            registrationPasswordString = registrationPassword.getText().toString();
            registrationRetypePasswordString = registrationRetypePassword.getText().toString();
            registrationDisplayNameString = registrationDisplayName.getText().toString();
            registrationAlignmentString = registrationAlignmentSpinner.getSelectedItem().toString();
            registrationLocationOfOriginString = registrationLocationOfOrigin.getText().toString();

            if( !(registrationPasswordString.equals(registrationRetypePasswordString)) ){
                registrationRetypePassword.setError("The passwords do not match");
            }
            else{
                registerNewUser(registrationUserNameString, registrationPasswordString,
                        registrationDisplayNameString, registrationAlignmentString);
            }
        }
        catch (NullPointerException e){
            Toast.makeText(this,"Looks like one of the fields was not entered!",Toast.LENGTH_SHORT).show();
        }

    }

    public void registerNewUser(String newUsername,String newPassword,
                                String newDisplayName, String newAlignment){

        boolean usernameAndPasswordProvided = true;

        if(  newUsername.isEmpty() ){
            registrationUserName.setError("Username Is Missing");
            usernameAndPasswordProvided = false;
        }

        if( newPassword.isEmpty() ){
            registrationPassword.setError("Password is Missing");
            usernameAndPasswordProvided = false;
        }

        if(usernameAndPasswordProvided){
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            user.put("userAlignment",newAlignment);
            user.put("userDisplayName",newDisplayName);
            user.put("userLocationOfOrigin",registrationLocationOfOriginString);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN:
                                registrationUserName.setError("Username Already Taken");
                                break;
                            case ParseException.USERNAME_MISSING:
                                registrationUserName.setError("Username Is Missing");
                                break;
                            case ParseException.PASSWORD_MISSING:
                                registrationPassword.setError("Password Missing");
                                break;
                            case ParseException.CONNECTION_FAILED:
                                registrationButton.setError("Connection Failed");
                                break;
                            default:
                                registrationButton.setError("Sign Up Failed! :(");
                                break;
                        }
                    }
                    else{
                        user.saveInBackground();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestClass");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    objectsWereRetrievedSuccessfully(objects);
                                    Intent intent = new Intent(RegistrationActivity.this,
                                            QuestListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    objectRetrievalFailed(e);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void objectRetrievalFailed(ParseException e) {
        switch (e.getCode()){
            case ParseException.OBJECT_NOT_FOUND:
                Toast.makeText(this,"No quests are available",Toast.LENGTH_SHORT).show();
                break;
            case ParseException.CONNECTION_FAILED:
                Toast.makeText(this,"Connection Failed!",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void objectsWereRetrievedSuccessfully(List<ParseObject> objects) {
        Vector<ParseObject> userQuestVector = new Vector<ParseObject>();

        for(int i = 0; i < objects.size(); i++){
            userQuestVector.add(new ParseObject("QuestClass_" + user.getObjectId()));
        }

        for(int i = 0; i < userQuestVector.size(); i++){
            userQuestVector.get(i).put("questNumber",objects.get(i).getInt("questNumber"));
            userQuestVector.get(i).put("questName",objects.get(i).getString("questName"));
            userQuestVector.get(i).put("questAlignment",objects.get(i).getString("questAlignment"));
            userQuestVector.get(i).put("questGiver",objects.get(i).getString("questGiver"));
            userQuestVector.get(i).put("questStatus",objects.get(i).getString("questStatus"));
            userQuestVector.get(i).put("questGiverLatitude",objects.get(i).getDouble("questGiverLatitude"));
            userQuestVector.get(i).put("questGiverLongitude",objects.get(i).getDouble("questGiverLongitude"));
            userQuestVector.get(i).put("questDetails",objects.get(i).getString("questDetails"));
            userQuestVector.get(i).put("questLatitude",objects.get(i).getDouble("questLatitude"));
            userQuestVector.get(i).put("questLongitude",objects.get(i).getDouble("questLongitude"));

            userQuestVector.get(i).saveInBackground();
        }
    }
}
