package hci.hal9000;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;



public class RoutinesFragment extends Fragment {
    private GridView gv;
    private ArrayList<Routine> routines = new ArrayList<>();
    public RoutineAdapter routineAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.routines_fragment, container, false);
    }

        @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            gv =  getActivity().findViewById(R.id.gridviewRoutines);
            routineAdapter = new RoutineAdapter(getActivity().getApplicationContext(), routines);
            gv.setAdapter(routineAdapter);


        Api.getInstance(getContext()).getRoutines(new Response.Listener<ArrayList<Routine>>() {
            @Override
            public void onResponse(ArrayList<Routine> response) {
                getActivity().getWindow().getDecorView().setBackgroundResource(R.drawable.white_back);
                gv.setAdapter(new RoutineAdapter(getContext(),response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FragmentActivity act = getActivity();
                if(act != null){
                    act.getWindow().getDecorView().setBackgroundResource(R.drawable.no_internet_background);
                }

            }
        });

    }
}

