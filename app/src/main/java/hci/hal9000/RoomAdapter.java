package hci.hal9000;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RoomAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Room> rooms;

    // 1
    public RoomAdapter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    // 2
    @Override
    public int getCount() {
        return rooms.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Room room = rooms.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.linearlayout_room, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art_room);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_room_name);
        final String meta = room.getMeta();

        setResource(meta,imageView,parent);

        //imageView.setImageResource(R.drawable.);
        nameTextView.setText(room.getName());

        return convertView;
    }

    private void setResource(String meta, ImageView imageView,ViewGroup parent) {
        String resource = meta.replaceAll("[{}]","");
        imageView.setImageResource(parent.getContext().getResources().getIdentifier(resource, "drawable", parent.getContext().getPackageName()));
    }
}
