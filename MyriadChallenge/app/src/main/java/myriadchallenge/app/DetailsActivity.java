package myriadchallenge.app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBoundsCreator;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;

public class DetailsActivity extends FragmentActivity {

    String[] questsNames = new String[] {"Bandits in the Woods", "Special Delivery",
            "Filthy Mongrel"};

    String[] questsAlignments = new String[] {"GOOD", "NEUTRAL", "EVIL"};

    String[] questsGivers = new String[] {"HotDog The Bounty Hunter", "Sir Jimmy The Swift",
            "Prince Jack, The Iron Horse"};

    String[] questsDetails = new String[] {"The famed bounty hunter HotDog has requested the aid of a hero in ridding the woods of terrifying bandits who have thus far eluded his capture, as he is actually a dog, and cannot actually grab things more than 6 feet off the ground.",
            "Sir Jimmy was once the fastest man in the kingdom, brave as any soldier and wise as a king. Unfortunately, age catches us all in the end, and he has requested that I, his personal scribe, find a hero to deliver a package of particular importance--and protect it with their life.",
            "That strange dog that everyone is treating like a bounty-hunter must go. By the order of Prince Jack, that smelly, disease ridden mongrel must be removed from our streets by any means necessary. He is disrupting the lives of ordinary citizens, and it's just really weird. Make it gone."};


    double[][] questsLocations = new double[][]{
            {46.908588, -96.808991},
            {46.8657639, -96.7363173},
            {46.892386,-96.799669}
            };

    double[][] questGiversLocations = new double[][]{
            {46.8541979, -96.8285138},
            {46.8739748, -96.806112},
            {46.8739748, -96.806112}
    };

    private int questNumber = 0xFF;

    private GoogleMap mMap;

    TextView tvQuestName, tvQuestAlignment, tvQuestGiver, tvQuestDetails, tvQuestLocation;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_details_activity);

        Intent intent = getIntent();

        String questName = intent.getExtras().getString("Quest Name");

        for(int i = 0; i < questsNames.length; i++){
            if( questName.equals(questsNames[i]) ){
                questNumber = i;
            }
        }

        tvQuestName = (TextView) findViewById(R.id.quest_name);
        tvQuestAlignment = (TextView) findViewById(R.id.quest_alignment);
        tvQuestGiver = (TextView) findViewById(R.id.quest_giver);
        tvQuestDetails = (TextView) findViewById(R.id.quest_details);


        tvQuestName.setText(" " + questsNames[questNumber]);
        tvQuestName.setTypeface(Typeface.SERIF, Typeface.BOLD);


        tvQuestAlignment.setText(" " + questsAlignments[questNumber] + "\n");
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

        tvQuestGiver.setText(" " + questsGivers[questNumber] + "\n");

        tvQuestDetails.setText("\t" + questsDetails[questNumber] + "\n");

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setUpMapIfNeeded();
    }

    public void setUpMapIfNeeded(){

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap(){

        LatLng mQuestLocationLatLng = new LatLng(questsLocations[questNumber][0],
                questsLocations[questNumber][1]);

        LatLng mQuestGiverLatLng = new LatLng(questGiversLocations[questNumber][0],
                questGiversLocations[questNumber][1]);

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
