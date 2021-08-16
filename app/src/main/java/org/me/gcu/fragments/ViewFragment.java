package org.me.gcu.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.me.gcu.MainActivity;
import org.me.gcu.R;

public class ViewFragment extends Fragment {

    public ViewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragments, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view == null) {
            return;
        }
        //recycle view for the tabs
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //initiates fragment activity
        FragmentActivity activity = getActivity();
        Bundle bundle = this.getArguments();
        //shows the appropriate tab
        if (activity instanceof MainActivity && bundle != null && bundle.containsKey("tab")) {
            MainActivity mainActivity = (MainActivity) getActivity();
            assert mainActivity != null;
            recyclerView.setAdapter(mainActivity.getAdapter(bundle.getInt("tab")));
        }
    }
}
