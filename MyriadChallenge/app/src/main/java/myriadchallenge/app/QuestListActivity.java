package myriadchallenge.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class QuestListActivity extends ListActivity {

    private ListView questList;

    String[] questsName = new String[] {"Bandits in the Woods", "Special Delivery",
            "Filthy Mongrel"};

    String[] questsAlignment = new String[] {"GOOD", "NEUTRAL", "EVIL"};

    String[] questsGiver = new String[] {"HotDogg The Bounty Hunter", "Sir Jimmy The Swift",
            "Prince Jack, The Iron Horse"};

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.quest_list_activity);

        questList = (ListView) findViewById(android.R.id.list);

        questList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                questsName));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(QuestListActivity.this, DetailsActivity.class);
        intent.putExtra("quest number",position);
        startActivity(intent);
    }

}

