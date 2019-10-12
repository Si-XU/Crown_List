package au.anu.u6807681.listcrown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     *   include all items
     */
    private ArrayList<Item> itemList = new ArrayList<>();
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initListItems();
    }

    public void initListItems() {
        for (int i = 0; i < 2; i++) {
            Item addItem = new Item("test "+itemList.size(), "this is test "+itemList.size());
            itemList.add(addItem);
        }
    }

    public void clickAdd(View V) {
        Item addItem = new Item("test "+itemList.size(), "this is test "+itemList.size());
        itemList.add(addItem);
        adapter = new ItemAdapter(itemList, MainActivity.this);
        ListView listView = (ListView) findViewById(R.id.itemList);
        listView.setAdapter(adapter);
    }
}
