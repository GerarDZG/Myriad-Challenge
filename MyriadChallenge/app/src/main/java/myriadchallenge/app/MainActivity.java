package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity {

    String[] questsNames = new String[] {"Bandits in the Woods", "Special Delivery",
            "Filthy Mongrel"};

    String[] questsAlignment = new String[] {"GOOD", "NEUTRAL", "EVIL"};

    private Button signInButton, registerButton;

    ParseUser userParse;

    EditText enteredUsername, enteredPassword;

    SharedPreferences logInPreference, usernamePreference;
    SharedPreferences.Editor logInEditor, usernameEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button)findViewById(R.id.register_button);
        signInButton = (Button)findViewById(R.id.sign_in_button);

        logInPreference = this.getSharedPreferences("Log In", MODE_PRIVATE);
        logInEditor = logInPreference.edit();

        usernamePreference = this.getSharedPreferences("Username", MODE_PRIVATE);
        usernameEditor = usernamePreference.edit();

        logInEditor.putBoolean("User Logged In", false);
        logInEditor.commit();

        try{
            Parse.initialize(this,"terKUGbzwzm9bynvp7g2RFgozgzyLlV6HyZrF5Hm",
                "NxhfeMyPNSIV0aosXtf9QGTlYbjbufWmeJLUwyRt");
        }
        catch (NetworkOnMainThreadException e){
            // Parse has already been initialized.
        }

        userParse = new ParseUser();
        signInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (view == signInButton) {
                    loginButtonPressed();
                }
            }
        });

        registerButton.setOnClickListener((new View.OnClickListener() {

            public void onClick(View view) {
                if(view == registerButton){
                    registerButtonPressed();
                }
            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        String username;
        if (logInPreference.getBoolean("User Logged In",false)){
            username = new String(usernamePreference.getString("Username","User"));
            userParse.logOut();
            enteredUsername.setText("");
            enteredPassword.setText("");
            Toast.makeText(this,username + " was logged out",0).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loginButtonPressed(){

        String usernameString, passwordString;

        enteredUsername = (EditText)findViewById(R.id.username);
        enteredPassword = (EditText)findViewById(R.id.password);

        try{
            usernameString = enteredUsername.getText().toString();
            passwordString = enteredPassword.getText().toString();

            boolean usernameAndPasswordProvided = true;

            if(  usernameString.isEmpty() ){
                enteredUsername.setError("Username Is Missing");
                usernameAndPasswordProvided = false;
            }

            if( passwordString.isEmpty() ){
                enteredPassword.setError("Password is Missing");
                usernameAndPasswordProvided = false;
            }

            if(usernameAndPasswordProvided){
                logInUser(usernameString,passwordString);
            }
        }
        catch(NullPointerException e){
            Toast.makeText(this, "Looks like one of the fields was not entered!", 0).show();
        }
    }

    public void logInUser(String user, String pass){

        final String finalUser = user;
        userParse.logInInBackground(user,pass,new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(parseUser != null){
                    logInEditor.putBoolean("User Logged In", true);
                    logInEditor.commit();

                    usernameEditor.putString("Username",finalUser);
                    usernameEditor.commit();

                    Intent intent = new Intent(MainActivity.this, QuestListActivity.class);
                    startActivity(intent);
                }
                else{
                    switch (e.getCode()) {
                        default:
                        case ParseException.OBJECT_NOT_FOUND:
                            enteredPassword.setText("");
                            enteredUsername.setText("");
                            enteredPassword.setError("Incorrect Username/Password Combination " +
                                    "or User Not Registered");
                            break;
                        case ParseException.CONNECTION_FAILED:
                            Toast.makeText(getApplicationContext(),"Connection Failed",0).show();
                            break;
                    }
                }
            }
        });
    }

    public void registerButtonPressed(){

        EditText enteredUsername = (EditText)findViewById(R.id.username);
        EditText enteredPassword = (EditText)findViewById(R.id.password);
        String usernameString, passwordString;
        try{
            usernameString = enteredUsername.getText().toString();
        }
        catch (NullPointerException e){
            usernameString = "";
        }

        try{
            passwordString = enteredPassword.getText().toString();
        }catch (NullPointerException e){
            passwordString = "";
        }

        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        intent.putExtra("User Name", usernameString);
        intent.putExtra("Password", passwordString);
        startActivity(intent);
    }
}
