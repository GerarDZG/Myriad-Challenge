package myriadchallenge.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.util.List;

public class DetailsActivity extends FragmentActivity {

    private int questNumber;

    private GoogleMap mMap;

    TextView tvQuestName, tvQuestAlignment, tvQuestGiver, tvQuestDetails;

    Button acceptButton;

    ParseObject currentQuest;

    ParseUser user;

    String questId;

    final int INVALID_QUEST_NUMBER = 0xFFFFFFFF;

    String questStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_details_activity);

        Intent intent = getIntent();

        user = ParseUser.getCurrentUser();

        questNumber = INVALID_QUEST_NUMBER;

        try{
            questId = intent.getExtras().getString("Quest ID");
        }
        catch (NullPointerException e){
            questId = null;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestClass_" + user.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objectsWereRetrievedSuccessfully(objects);
                    showQuestDetails();
                }
                else {
                    objectRetrievalFailed(e);
                }
            }
        });

        acceptButton = (Button)findViewById(R.id.accept_button);
        acceptButton.setClickable(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(questNumber != INVALID_QUEST_NUMBER){
            setUpMapIfNeeded();
        }
    }

    private void showQuestDetails() {
        if(questNumber != INVALID_QUEST_NUMBER){

            if( questStatus.equals("AVAILABLE") ){
                acceptButton.setText("Accept Quest");
            }
            else if( questStatus.equals("ACCEPTED") ){
                acceptButton.setText("Quest Accepted! Press to Complete");
            }
            else{
                acceptButton.setText("Quest Completed!");
                acceptButton.setClickable(false);
            }

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view == acceptButton){
                        if( questStatus.equals("AVAILABLE") ){
                            acceptButton.setText("Quest Accepted! Press to Complete");
                            currentQuest.put("questStatus", "ACCEPTED");
                            questStatus = "ACCEPTED";
                        }
                        else{
                            acceptButton.setText("Quest Completed!");
                            currentQuest.put("questStatus", "COMPLETED");
                            acceptButton.setClickable(false);
                        }

                        currentQuest.saveInBackground();
                    }
                }
            });

            tvQuestName = (TextView) findViewById(R.id.quest_name);
            tvQuestAlignment = (TextView) findViewById(R.id.quest_alignment);
            tvQuestGiver = (TextView) findViewById(R.id.quest_giver);
            tvQuestDetails = (TextView) findViewById(R.id.quest_details);


            tvQuestName.setText(" " + currentQuest.getString("questName"));
            tvQuestName.setTypeface(Typeface.SERIF, Typeface.BOLD);


            tvQuestAlignment.setText("\n " + currentQuest.getString("questAlignment") + "\n");
            tvQuestAlignment.setTypeface(null, Typeface.BOLD);
            switch(questNumber){
                case 0:
                    tvQuestAlignment.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                    break;
                default:
                    break;
                case 1:
                    tvQuestAlignment.setTextColor(getResources().getColor(android.R.color.black));
                    break;
                case 2:
                    tvQuestAlignment.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;
            }

            tvQuestGiver.setText(" " + currentQuest.getString("questGiver") + "\n");

            tvQuestDetails.setText("\t" + currentQuest.getString("questDetails") + "\n");

            setUpMapIfNeeded();
        }
    }

    private void objectRetrievalFailed(ParseException e) {
        questNumber = INVALID_QUEST_NUMBER;

        switch (e.getCode()){
            case ParseException.OBJECT_NOT_FOUND:
                Toast.makeText(this, "No quests are available", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case ParseException.CONNECTION_FAILED:
                Toast.makeText(this,"Connection Failed!",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Toast.makeText(this,"Something went wrong :/", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void objectsWereRetrievedSuccessfully(List<ParseObject> object) {
        for(int i = 0; i < object.size(); i++){
            if(object.get(i).getObjectId().equals(questId)){
                currentQuest = object.get(i);
                break;
            }
        }

        questNumber = currentQuest.getInt("questNumber");
        questStatus = currentQuest.getString("questStatus");
    }

    public void setUpMapIfNeeded(){
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    if(questNumber != INVALID_QUEST_NUMBER){
                    setUpMap();
                    }
                }
            }
    }

    private void setUpMap(){
        LatLng mQuestLocationLatLng = new LatLng(currentQuest.getDouble("questLatitude"),
                currentQuest.getDouble("questLongitude"));

        LatLng mQuestGiverLatLng = new LatLng(currentQuest.getDouble("questGiverLatitude"),
                currentQuest.getDouble("questGiverLongitude"));

        MarkerOptions mQuestLocationMarkerOptions =
                new MarkerOptions().position(mQuestLocationLatLng).title("Quest Location");

        MarkerOptions mQuestGiverMarkerOptions =
                new MarkerOptions().position(mQuestGiverLatLng).title("Quest Giver");

        mMap.addMarker(mQuestLocationMarkerOptions);
        mMap.addMarker(mQuestGiverMarkerOptions);

        LatLngBounds.Builder mBuilder = new LatLngBounds.Builder();

        mBuilder.include(mQuestLocationMarkerOptions.getPosition());
        mBuilder.include(mQuestGiverMarkerOptions.getPosition());

        LatLngBounds mBounds = mBuilder.build();

        // bounding box width in pixels (px)
        int width = 150;

        // bounding box height in pixels (px)
        int height = 150;

        // No additional size restrictions on newLatLngBounds
        int padding = 0;

        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(mBounds,width,height,padding);

        mMap.animateCamera(mCameraUpdate);
    }
}
