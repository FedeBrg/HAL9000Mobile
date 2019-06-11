package hci.hal9000;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Device> devices;

    public DeviceAdapter(Context context, ArrayList<Device> devices) {
        this.context = context;
        this.devices = devices;
    }


    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Device device = devices.get(position);
        if(convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.linearlayout_device, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art_device);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_device_name);

        //imageView.setImageResource(R.drawable.air_conditioner);
        setResource(device.getMeta(),imageView,parent);
        nameTextView.setText(device.getName());

        return convertView;
    }

    private void setResource(String meta, ImageView imageView,ViewGroup parent) {
        String resource = meta.replaceAll("[{}]","");
        imageView.setImageResource(parent.getContext().getResources().getIdentifier(resource, "drawable", parent.getContext().getPackageName()));
    }
}
