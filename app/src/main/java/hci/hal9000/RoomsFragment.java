package hci.hal9000;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class RoomsFragment extends Fragment {
    private ArrayList<Room> rooms = new ArrayList<>();
    public RoomAdapter roomAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rooms_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        View grid = getActivity().findViewById(R.id.gridview);
        GridView gridView = (GridView) grid;
        roomAdapter = new RoomAdapter(getActivity().getApplicationContext(), rooms);
        gridView.setAdapter(roomAdapter);

        View fab = getActivity().findViewById(R.id.fab_room);
        FloatingActionButton button = (FloatingActionButton) fab;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CreateRoom.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        Intent intent = getActivity().getIntent();
        if(intent != null){
            String name = intent.getStringExtra(CreateRoom.roomname);
            if(name != null) {
                Room room = new Room(name, R.drawable.adult_bedroom);
                rooms.add(room);
                roomAdapter.notifyDataSetChanged();
            }
        }
    }
}
