package au.anu.u6807681.listcrown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List itemList, Context context) {
        super(context,R.layout.singleitem, itemList);
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.singleitem, parent, false);
        }
        // Now we can fill the layout with the right values
        TextView tv = (TextView) convertView.findViewById(R.id.name);
        EditText ev = (EditText) convertView.findViewById(R.id.description);
        Item p = itemList.get(position);

        tv.setText(p.keyword);
        ev.setText("" + p.description);

        return convertView;
    }
}
