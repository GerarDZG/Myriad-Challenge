package myriadchallenge.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    List<ParseObject> quests, questListFromQuestClass;

    ParseUser user;

    Button filterButton;

    Spinner questTypeSinner;

    ArrayAdapter<String> adapter;

    boolean listViewAdapterSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestClass_" + user.getObjectId());
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

    @Override
    protected void onResume() {
        super.onResume();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestClass_" + user.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objectsWereRetrievedSuccessfully(objects);
                    updateQuestList(questTypeSinner.getSelectedItem().toString(), alignment);
                }
                else {
                    objectRetrievalFailed(e);
                }
            }
        });
    }
    

    private void updateQuestList(String questType, String updateAlignment) {
        Vector<String> vector = new Vector<String>();
        if( !updateAlignment.equals("NEUTRAL") ){
            for(int i = 0; i < quests.size(); i++){
                if( quests.get(i).getString("questStatus").equals(questType)
                        && quests.get(i).getString("questAlignment").equals(updateAlignment) ){
                    vector.add(quests.get(i).getString("questName"));
                }
            }
        }
        else{
            for(int i = 0; i < quests.size(); i++){
                if( quests.get(i).getString("questStatus").equals(questType) ){
                    vector.add(quests.get(i).getString("questName"));
                }
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
            startActivity(intent);
            finish();
            return true;
        }
        else if (id == R.id.action_logout) {
            logOutUser();
            return true;
        }
        else if (id == R.id.action_get_more_quests){
            pullQuestList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pullQuestList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestClass");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objectsWereRetrievedSuccessfullyForRefreshQuestList(objects);
                    updateQuestList(questTypeSinner.getSelectedItem().toString(), alignment);
                }
                else {
                    objectRetrievalFailed(e);
                }
            }
        });
    }

    private void objectsWereRetrievedSuccessfullyForRefreshQuestList(List<ParseObject> objects) {
        questListFromQuestClass = objects;

        // if the number of quests that the user holds and the number of quests in QuestClass are
        // the same, then the user holds all the quests.
        if( quests.size() == questListFromQuestClass.size() ){
            Toast.makeText(this,"There are no more quests at the moment",Toast.LENGTH_SHORT).show();
        }
        // else if the quests the user holds are less than the number of quests in QuestClass, then
        // the user is missing some quests, so retrieve them from the main copy to the user's copy,
        // and save the user's copy.
        else if( quests.size() < questListFromQuestClass.size() ){
            int numberOfNewQuests = questListFromQuestClass.size() - quests.size();
            int numberOfUserQuests = quests.size();
            for(int i = 0; i < numberOfNewQuests; i++){
                quests.add(new ParseObject("QuestClass_" + user.getObjectId()));
            }

            for(int i = 0; i < numberOfNewQuests; i++){
                quests.get(i+numberOfUserQuests).put("questNumber",
                        questListFromQuestClass.get(i+numberOfUserQuests).getInt("questNumber"));

                quests.get(i+numberOfUserQuests).put("questName",
                        questListFromQuestClass.get(i+numberOfUserQuests).getString("questName"));

                quests.get(i+numberOfUserQuests).put("questAlignment",
                        questListFromQuestClass.get(i+numberOfUserQuests).getString("questAlignment"));

                quests.get(i+numberOfUserQuests).put("questGiver",
                        questListFromQuestClass.get(i+numberOfUserQuests).getString("questGiver"));

                quests.get(i+numberOfUserQuests).put("questStatus",
                        questListFromQuestClass.get(i+numberOfUserQuests).getString("questStatus"));

                quests.get(i+numberOfUserQuests).put("questGiverLatitude",
                        questListFromQuestClass.get(i+numberOfUserQuests).getDouble("questGiverLatitude"));

                quests.get(i+numberOfUserQuests).put("questGiverLongitude",
                        questListFromQuestClass.get(i+numberOfUserQuests).getDouble("questGiverLongitude"));

                quests.get(i+numberOfUserQuests).put("questDetails",
                        questListFromQuestClass.get(i+numberOfUserQuests).getString("questDetails"));

                quests.get(i+numberOfUserQuests).put("questLatitude",
                        questListFromQuestClass.get(i+numberOfUserQuests).getDouble("questLatitude"));

                quests.get(i+numberOfUserQuests).put("questLongitude",
                        questListFromQuestClass.get(i+numberOfUserQuests).getDouble("questLongitude"));

                quests.get(i+numberOfUserQuests).saveInBackground();
            }
            

            updateQuestList(questTypeSinner.getSelectedItem().toString(), alignment);
            Toast.makeText(this,"Update Successful",Toast.LENGTH_SHORT).show();
        }
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

