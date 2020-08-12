package com.schoolpathram.schoolpathramdotcom.ui.classroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.schoolpathram.schoolpathramdotcom.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SamagraWebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SamagraWebFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    WebView webView;

    public SamagraWebFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SamagraWebFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SamagraWebFragment newInstance(String param1, String param2) {
        SamagraWebFragment fragment = new SamagraWebFragment();
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
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_samagra_web, container, false);

        // Inflate the layout for this fragment
        webView = (WebView) v.findViewById(R.id.samagraWeb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("www.samagra.kite.kerala.gov.in");

        return v;
    }
}