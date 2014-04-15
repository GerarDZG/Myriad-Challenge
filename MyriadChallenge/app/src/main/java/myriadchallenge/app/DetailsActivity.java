package myriadchallenge.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;


public class DetailsActivity extends Activity {

    String[] questsNames = new String[] {"Bandits in the Woods", "Special Delivery",
            "Filthy Mongrel"};

    String[] questsAlignments = new String[] {"GOOD", "NEUTRAL", "EVIL"};

    String[] questsGivers = new String[] {"HotDogg The Bounty Hunter", "Sir Jimmy The Swift",
            "Prince Jack, The Iron Horse"};

    String[] questsDetails = new String[] {"The famed bounty hunter HotDog has requested the aid of a hero in ridding the woods of terrifying bandits who have thus far eluded his capture, as he is actually a dog, and cannot actually grab things more than 6 feet off the ground.",
            "Sir Jimmy was once the fastest man in the kingdom, brave as any soldier and wise as a king. Unfortunately, age catches us all in the end, and he has requested that I, his personal scribe, find a hero to deliver a package of particular importance--and protect it with their life.",
            "That strange dog that everyone is treating like a bounty-hunter must go. By the order of Prince Jack, that smelly, disease ridden mongrel must be removed from our streets by any means necessary. He is disrupting the lives of ordinary citizens, and it's just really weird. Make it gone."};

    String[] questsLocations = new String[] {"(46.908588, -96.808991)", "(46.8657639, -96.7363173)",
            "(46.892386,-96.799669)"};

    private int questNumber = 0xFF;

    TextView tvQuestName, tvQuestAlignment, tvQuestGiver, tvQuestDetails, tvQuestLocation;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_details_activity);

        Intent intent = getIntent();

        questNumber = intent.getExtras().getInt("quest number");

        tvQuestName = (TextView) findViewById(R.id.quest_name);
        tvQuestAlignment = (TextView) findViewById(R.id.quest_alignment);
        tvQuestGiver = (TextView) findViewById(R.id.quest_giver);
        tvQuestDetails = (TextView) findViewById(R.id.quest_details);
        tvQuestLocation = (TextView) findViewById(R.id.quest_location);

        tvQuestName.setText(questsNames[questNumber] + "\n");
        tvQuestName.setTypeface(null, Typeface.BOLD);


        tvQuestAlignment.setText(questsAlignments[questNumber] + "\n");
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

        tvQuestGiver.setText(questsGivers[questNumber] + "\n");

        tvQuestDetails.setText(questsDetails[questNumber] + "\n");

        tvQuestLocation.setText(questsLocations[questNumber]);
    }
}
