package myriadchallenge.app;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class QuestListActivity extends ListActivity {

    ListView questList;
    TextView tvDispName, tvLocation;
    SharedPreferences settings;

    final int BUTTON_PRESS = 0xFF;

    String[] questsName = new String[] {"Bandits in the Woods", "Special Delivery",
            "Filthy Mongrel"};

    String[] questsAlignment = new String[] {"GOOD", "NEUTRAL", "EVIL"};

    String[] questsGiver = new String[] {"HotDogg The Bounty Hunter", "Sir Jimmy The Swift",
            "Prince Jack, The Iron Horse"};

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        settings = this.getSharedPreferences("settingsMyriad", MODE_PRIVATE);

        setContentView(R.layout.quest_list_activity);

        questList = (ListView) findViewById(android.R.id.list);

        tvDispName = (TextView) findViewById(R.id.shared_preference_display_name);
        tvLocation = (TextView) findViewById(R.id.shared_preference_location);

        Intent intent = getIntent();

        String displayName, locationOfOrigin;

        try{
            displayName = intent.getExtras().getString("Display Name", "Lancelot");
            settings.edit().putString("Display Name",displayName);
            settings.edit().commit();
        }
        catch (NullPointerException npe){
            try{
                displayName = settings.getString("Display Name", "Lancelot");
            }
            catch (NullPointerException e){
                displayName = "Lancelot";
            }
        }

        tvDispName.setText(displayName);


        try{
            locationOfOrigin = intent.getExtras().getString("Location of Origin", "USA");
            settings.edit().putString("Location of Origin", locationOfOrigin);
            settings.edit().commit();
        }
        catch (NullPointerException npe){
            try{
                locationOfOrigin = settings.getString("Location of Origin", "USA");
            }
            catch (NullPointerException e){
                locationOfOrigin = "USA";
            }
        }

        tvLocation.setText(locationOfOrigin);


         //TODO: only show quests for the users alignment
        questList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                questsName));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(QuestListActivity.this, DetailsActivity.class);
        intent.putExtra("quest number",position);
        startActivity(intent);
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
            Intent intent = new Intent(QuestListActivity.this, SettingsActivity.class);
            startActivityForResult(intent,BUTTON_PRESS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

