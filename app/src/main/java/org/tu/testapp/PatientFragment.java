package org.tu.testapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tu.testapp.placeholder.PlaceholderContent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A fragment representing a list of Items.
 */
public class PatientFragment extends Fragment {

    private String patientId;
    private AtomicBoolean isActive = new AtomicBoolean(true);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            patientId = getArguments().getString("patient_to_remove");
            if (patientId != null) {
                PlaceholderContent.removeItem(patientId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new PatientRecyclerViewAdapter(PlaceholderContent.ITEMS, isActive, view));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        isActive.set(false);
        super.onDestroy();
    }
}