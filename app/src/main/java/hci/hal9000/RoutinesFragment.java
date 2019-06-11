package hci.hal9000;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class RoutinesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.routines_fragment, container, false);
    }

//        @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
//        NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//    }
}

