package com.example.musicplayer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationBarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ImageButton btn_setup;
    private ImageButton btn_player;
    private ImageButton btn_albumlist;
    private ImageButton btn_eq;

    private Context context;

    public NavigationBarFragment() {
        // Required empty public constructor
    }

    public NavigationBarFragment(Context context){
        this.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavigationBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigationBarFragment newInstance(String param1, String param2) {
        NavigationBarFragment fragment = new NavigationBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);

        btn_setup = view.findViewById(R.id.btn_setup);
        btn_player = view.findViewById(R.id.btn_player);
        btn_albumlist = view.findViewById(R.id.btn_albumlist);
        btn_eq = view.findViewById(R.id.btn_eq);

        btn_player.setOnClickListener(v ->{
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.switchFragment(MainActivity.PLAYER_TAG);
            }
        });

        btn_albumlist.setOnClickListener(v ->{
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.switchFragment(MainActivity.ALBUM_TAG);
            }
        });


        return view;
    }
}