package au.anu.u6807681.listcrown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.List;

public class MainAdapter extends ArrayAdapter<Event> {
    private List<Event> eventList;
    private Context context;

    public MainAdapter(List eventList, Context context) {
        super(context,R.layout.singleitem, eventList);
        this.eventList = eventList;
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
        Event p = eventList.get(position);
        String[] texts = ((String) p.getData()).split("\\|");
        if(texts.length > 1) {
            tv.setText(texts[0]);
            ev.setText(texts[1]);
        }

        return convertView;
    }
}
