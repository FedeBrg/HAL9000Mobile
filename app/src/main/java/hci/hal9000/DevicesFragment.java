package hci.hal9000;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class DevicesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.devices_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);


        View fab = getActivity().findViewById(R.id.fab_devices);
        FloatingActionButton button = (FloatingActionButton) fab;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CreateDevice.class);
                    startActivity(intent);
                }
            });
        }

        View lightsv = getActivity().findViewById(R.id.lights_bttn);
        Button lights = (Button) lightsv;
        if(lights != null){
            lights.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LightDetails.class);
                    startActivity(intent);
                }
            });
        }

        View fridgev = getActivity().findViewById(R.id.fridge_bttn);
        Button fridge = (Button) fridgev;
        if(fridge != null){
            fridge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),FridgeDetails.class);
                    startActivity(intent);
                }
            });
        }
    }

//    public void lights_details(View view){
//
//
//
//    }
}
