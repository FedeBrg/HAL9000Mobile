package hci.hal9000;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
        final Device device = devices.get(position);
        if(convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.linearlayout_device, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art_device);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_device_name);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDeviceDetails(v,device);
            }
        });

        //imageView.setImageResource(R.drawable.air_conditioner);
        imageView.bringToFront();
        setResource(device.getMeta(),imageView,parent);
        nameTextView.setText(device.getName());

        return convertView;
    }

    private void setResource(String meta, ImageView imageView,ViewGroup parent) {
        String resource = meta.replaceAll("[{}]","");
        imageView.setImageResource(parent.getContext().getResources().getIdentifier(resource, "drawable", parent.getContext().getPackageName()));
    }


    public void viewDeviceDetails(View view,Device deviceO){
        String device = deviceO.getTypeId();
        Intent intent = new Intent();
        if(device.compareTo("go46xmbqeomjrsjr") == 0){

            intent =  new Intent(view.getContext(),LightDetails.class);
        }
        else if(device.compareTo("curtains") == 0){
            //return "eu0v2xgprrhhg41g";
        }
        else if(device.compareTo("air_conditioner") == 0){
            //return "li6cbv5sdlatti0j";
        }
        else if(device.compareTo("oven") == 0){
            //return "im77xxyulpegfmv8";
        }
        else if(device.compareTo("timer") == 0){
            //return "ofglvd9gqX8yfl3l";
        }
        else if(device.compareTo("alarm") == 0){
            //return "mxztsyjzsrq7iaqc";
        }
        else if(device.compareTo("door") == 0){
            //return "lsf78ly0eqrjbz91";
        }
        else if(device.compareTo("rnizejqr2di0okho") == 0){
            intent = new Intent(view.getContext(),FridgeDetails.class);
        }
        else{
            //return null;
        }

        intent.putExtra("id",deviceO.getId());
        view.getContext().startActivity(intent);
    }
}
