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
    TextView tvDispName, tvLocation, tvAlignment;
    String displayName, locationOfOrigin, alignment;
    SharedPreferences settings;

    String[] questsName = new String[] {"Bandits in the Woods", "Special Delivery",
            "Filthy Mongrel"};

    String[] questsAlignment = new String[] {"GOOD", "NEUTRAL", "EVIL"};

    String[] questsGiver = new String[] {"HotDog The Bounty Hunter", "Sir Jimmy The Swift",
            "Prince Jack, The Iron Horse"};

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        String[] quests;

        settings = this.getSharedPreferences("settingsMyriad", MODE_PRIVATE);

        setContentView(R.layout.quest_list_activity);

        questList = (ListView) findViewById(android.R.id.list);

        tvDispName = (TextView) findViewById(R.id.shared_preference_display_name);
        tvLocation = (TextView) findViewById(R.id.shared_preference_location);
        tvAlignment = (TextView) findViewById(R.id.shared_preference_alignment);

        SharedPreferences.Editor settingsEditor = settings.edit();

        Intent intent = getIntent();

        try{
            displayName = intent.getExtras().getString("Display Name", "Lancelot");
            settingsEditor.putString("Display Name", displayName);
            settingsEditor.commit();
        }
        catch (NullPointerException npe){
            try{
                displayName = settings.getString("Display Name", "Lancelot");
            }
            catch (NullPointerException e){
                displayName = "Lancelot";
            }
        }

        tvDispName.setText("\nDisplay Name: " + displayName);


        try{
            locationOfOrigin = intent.getExtras().getString("Location of Origin", "USA");
            settingsEditor.putString("Location of Origin", locationOfOrigin);
            settingsEditor.commit();
        }
        catch (NullPointerException npe){
            try{
                locationOfOrigin = settings.getString("Location of Origin", "USA");
            }
            catch (NullPointerException e){
                locationOfOrigin = "USA";
            }
        }

        tvLocation.setText("Location of Origin: " + locationOfOrigin);

        try{
            alignment = intent.getExtras().getString("Alignment", "NEUTRAL");
            settingsEditor.putString("Alignment", alignment);
            settingsEditor.commit();
        }
        catch (NullPointerException npe){
            try{
                alignment = settings.getString("Alignment", "NEUTRAL");
            }
            catch (NullPointerException e){
                alignment = "NEUTRAL";
            }
        }

        tvAlignment.setText("Alignment: " + alignment + "\n\n\n");

        // Is the user's alignment GOOD?
        if(alignment.equals(questsAlignment[0])){
            quests = new String[] {questsName[0]};
            questList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    quests));
        }
        // Is the user's alignment EVIL?
        else if(alignment.equals(questsAlignment[2])){
            quests = new String[] {questsName[2]};
            questList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    quests));
        }
        // Else: the user's alignment is NEUTRAL (Since NEUTRAL is the default, this is okay to do)
        else{
            questList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    questsName));
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String questName = l.getItemAtPosition(position).toString();
        Intent intent = new Intent(QuestListActivity.this, DetailsActivity.class);
        intent.putExtra("Quest Name",questName);
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
            intent.putExtra("Display Name",displayName);
            intent.putExtra("Location of Origin",locationOfOrigin);
            intent.putExtra("Alignment",alignment);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

