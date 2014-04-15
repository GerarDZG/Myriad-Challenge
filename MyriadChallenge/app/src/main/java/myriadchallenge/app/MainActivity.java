package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button logInButton;

    String userLancelot = "Lancelot";
    String userLancelotPassword = "arthurDoesntKnow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logInButton = (Button)findViewById(R.id.sign_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(view == logInButton){
                    loginButtonPressed();
                }
            }
        });

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

        EditText enteredUsername = (EditText)findViewById(R.id.username);
        String usernameString = enteredUsername.getText().toString();

        EditText enteredPassword = (EditText)findViewById(R.id.password);
        String passwordString = enteredPassword.getText().toString();

        boolean validUserAndPassword = true;

        if( !(usernameString.equals(userLancelot))
                || !(passwordString.equals(userLancelotPassword)) ){
            validUserAndPassword = false;
        }

        if(validUserAndPassword){
            Intent intent = new Intent(MainActivity.this, QuestListActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Incorrect Username/Password Combination", 0).show();
            enteredPassword.setText("");
            enteredUsername.setText("");
        }
    }
}
