package hci.hal9000;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceLogAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public DeviceLogAdapter(Context context, ArrayList<String> values){
        super(context,R.layout.device_log_list_item,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.device_log_list_item, parent, false);

        // Displaying a textview
        TextView textView = (TextView) rowView.findViewById(R.id.device_log_action);
        textView.setText(values.get(position));


        return rowView;
    }
}
