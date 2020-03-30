package au.anu.u6807681.listcrown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.HashMap;
import java.util.List;
/**
 * @author Si Xu
 * use adapter to list activity_view_item.xml in homepage
 *
 */
public class MainAdapter extends ArrayAdapter<HashMap<String, String>> {
    private List<HashMap<String, String>> eventList;
    private Context context;

    public MainAdapter(List eventList, Context context) {
        super(context,R.layout.activity_view_item, eventList);
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
            convertView = inflater.inflate(R.layout.activity_view_item, parent, false);
        }

        HashMap<String, String> p = eventList.get(position);
        TextView tv;

        tv =  convertView.findViewById(R.id.keyword);
        tv.setText(p.get("keyword"));
        tv =  convertView.findViewById(R.id.description);
        tv.setText(p.get("description"));
        tv =  convertView.findViewById(R.id.enddate);
        tv.setText(p.get("enddate"));
        tv =  convertView.findViewById(R.id.id);
        tv.setText(p.get("_id"));
        tv =  convertView.findViewById(R.id.state);
        tv.setText(p.get("state"));

        if(p.get("state").equals("undone")) {
            ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.imageView);
            imageViewIcon.setColorFilter(getContext().getResources().getColor(R.color.colorAccent));
        }
        if(p.get("state").equals("done")) {
            ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.imageView);
            imageViewIcon.setColorFilter(getContext().getResources().getColor(R.color.colorGreen));
        }

        return convertView;
    }
}
