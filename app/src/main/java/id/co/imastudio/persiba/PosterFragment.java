package id.co.imastudio.persiba;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PosterFragment extends Fragment {


    public PosterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_poster, container, false);

        PDFView pdfView = (PDFView) fragmentView.findViewById(R.id.pdv_view);
        pdfView.fromAsset("persibaposter.pdf").load();
        return fragmentView;    }

}
