package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegistrationActivity extends Activity{

    String registrationUserNameString, registrationPasswordString, registrationRetypePasswordString,
        registrationDisplayNameString, registrationAlignmentString;

    EditText registrationUserName, registrationPassword, registrationRetypePassword,
        registrationDisplayName;

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

            if( !(registrationPasswordString.equals(registrationRetypePasswordString)) ){
                registrationRetypePassword.setError("The passwords do not match");
            }
            else{
                registerNewUser(registrationUserNameString, registrationPasswordString,
                        registrationDisplayNameString, registrationAlignmentString);
            }

        }
        catch (NullPointerException e){
            Toast.makeText(this, "Looks like one of the fields was not entered!", 0).show();
        }

    }

    public void registerNewUser(String newUsername,String newPassword,
                                String newDisplayName, String newAlignment){

        final String finalDisplayName = newDisplayName;
        final String finalAlignment = newAlignment;
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
                        Intent intent = new Intent(RegistrationActivity.this, QuestListActivity.class);
                        intent.putExtra("Display Name", finalDisplayName);
                        intent.putExtra("Alignment", finalAlignment);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
