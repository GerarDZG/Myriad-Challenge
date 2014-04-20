package myriadchallenge.app;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Vector;


public class QuestListActivity extends ListActivity {

    ListView questList;
    TextView tvDispName, tvLocation, tvAlignment;
    String displayName, locationOfOrigin, alignment;

    List<ParseObject> quests;

    ParseUser user;

    Button filterButton;

    Spinner questTypeSinner;

    ArrayAdapter<String> adapter;

    boolean listViewAdapterSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        String[] quests;

        setContentView(R.layout.quest_list_activity);

        questList = (ListView) findViewById(android.R.id.list);

        tvDispName = (TextView) findViewById(R.id.shared_preference_display_name);
        tvLocation = (TextView) findViewById(R.id.shared_preference_location);
        tvAlignment = (TextView) findViewById(R.id.shared_preference_alignment);
        filterButton = (Button) findViewById(R.id.filter_quest_list_button);

        user = ParseUser.getCurrentUser();
        displayName = user.getString("userDisplayName");
        alignment = user.getString("userAlignment");
        locationOfOrigin = user.getString("userLocationOfOrigin");

        questTypeSinner = (Spinner)findViewById(R.id.quest_type_spinner);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestClass");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objectsWereRetrievedSuccessfully(objects);
                    updateQuestList("AVAILABLE", alignment);
                }
                else {
                    objectRetrievalFailed(e);
                }
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestList(questTypeSinner.getSelectedItem().toString(), alignment);
            }
        });

        tvDispName.setText("\nDisplay Name: " + displayName);
        tvLocation.setText("Location of Origin: " + locationOfOrigin);
        tvAlignment.setText("Alignment: " + alignment + "\n\n\n");
    }

    private void updateQuestList(String questType, String updateAlignment) {
        Vector<String> vector = new Vector<String>();
        if( !updateAlignment.equals("NEUTRAL") ){
            for(int i = 0; i < quests.size(); i++){
                if(quests.get(i).getString("questStatus").equals(questType)
                        && quests.get(i).getString("questAlignment").equals(updateAlignment)){
                    vector.add(quests.get(i).getString("questName"));
                }
            }
        }
        else{
            for(int i = 0; i < quests.size(); i++){
                vector.add(quests.get(i).getString("questName"));
            }
        }

        String[] questsArray = new String[vector.size()];

        for(int i = 0; i < vector.size(); i++){
            questsArray[i] = vector.get(i);
        }

        if(listViewAdapterSet){
            adapter.clear();
            adapter.notifyDataSetChanged();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                questsArray);

        questList.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String questName = l.getItemAtPosition(position).toString();
        String questId;
        boolean questNotFound = true;
        int i = 0;
        Intent intent = new Intent(QuestListActivity.this, DetailsActivity.class);
        do{
            if(quests.get(i).getString("questName").equals(questName)){
                questNotFound = false;
                questId = quests.get(i).getObjectId();
                intent.putExtra("Quest ID",questId);
                startActivity(intent);
            }

            i++;

        }while ( (i < quests.size()) && questNotFound );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quest_list_activity, menu);
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
            finish();
            return true;
        }
        else if (id == R.id.action_logout) {
            logOutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOutUser(){
        // finish activity, log out occurs in MainActivity
        user.logOut();
        finish();
    }

    private void objectsWereRetrievedSuccessfully(List<ParseObject> objects) {
        quests = objects;
    }

    private void objectRetrievalFailed(ParseException e){
        switch (e.getCode()){
            case ParseException.OBJECT_NOT_FOUND:
                Toast.makeText(this,"No quests are available",0).show();
                break;
            case ParseException.CONNECTION_FAILED:
                Toast.makeText(this,"Connection Failed!",0).show();
                break;
            default:
                break;
        }
    }

}

