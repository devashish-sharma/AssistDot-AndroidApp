package piyushjohnson.assistdot;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    String Title;

    public TodayFragment() {
        // Required empty public constructor
    }

    public static TodayFragment newInstance(String title){
        TodayFragment fragment = new TodayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null)
        {
            Title  = getArguments().getString("Title");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FrameLayout layout = ((FrameLayout)view);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(Title);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Fragment:","Stopped" + Title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment:","Destroyed" + Title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Fragment:","Detached" + Title);
    }
}
