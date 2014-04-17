package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

    String[] questsNames = new String[] {"Bandits in the Woods", "Special Delivery",
            "Filthy Mongrel"};

    String[] questsAlignment = new String[] {"GOOD", "NEUTRAL", "EVIL"};

    private Button signInButton, registerButton;

    Spinner spinnerAlignment;

    String userLancelot = "Lancelot";
    String userLancelotPassword = "arthurDoesntKnow";
    String questNameString, alignmentString = "NONE", dispNameString;

    int questNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button)findViewById(R.id.register_button);
        signInButton = (Button)findViewById(R.id.sign_in_button);

        spinnerAlignment = (Spinner)findViewById(R.id.disp_alignment_spinner);

        signInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(view == signInButton){
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

        if(alignmentString.equals("NONE")){
            alignmentString = "NEUTRAL";
        }

        boolean validUserAndPassword = true;

        if( !(usernameString.equals(userLancelot))
                || !(passwordString.equals(userLancelotPassword)) ){
            validUserAndPassword = false;
        }

        if(validUserAndPassword){
            Intent intent = new Intent(MainActivity.this, QuestListActivity.class);
            intent.putExtra("Display Name", dispNameString);
            intent.putExtra("Alignment", alignmentString);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Incorrect Username/Password Combination", 0).show();
            enteredPassword.setText("");
            enteredUsername.setText("");
        }
    }



    public void registerButtonPressed(){

        //TODO: What happens when a user wants to register
        EditText enteredUsername = (EditText)findViewById(R.id.username);
        String usernameString = enteredUsername.getText().toString();

        EditText enteredPassword = (EditText)findViewById(R.id.password);
        String passwordString = enteredPassword.getText().toString();

        //TODO add user/password to database
        EditText enteredDispName = (EditText)findViewById(R.id.disp_name);
        String dispNameString = enteredDispName.getText().toString();


        alignmentString = spinnerAlignment.getSelectedItem().toString();

        if(alignmentString.equals("NONE")){
            alignmentString = "NEUTRAL";
        }

        Intent intent = new Intent(MainActivity.this, QuestListActivity.class);
        intent.putExtra("Display Name", dispNameString);
        intent.putExtra("Alignment", alignmentString);
        startActivity(intent);
    }
}
