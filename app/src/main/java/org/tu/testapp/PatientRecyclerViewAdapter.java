package org.tu.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.tu.testapp.placeholder.PlaceholderContent.PlaceholderItem;
import org.tu.testapp.databinding.FragmentItemBinding;
import org.tu.testapp.util.SleepUtil;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 */
public class PatientRecyclerViewAdapter extends RecyclerView.Adapter<PatientRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;
    private final AtomicBoolean shouldSchedulePatients;
    private final Map<String, ViewHolder> itemHolders;

    public PatientRecyclerViewAdapter(List<PlaceholderItem> items, AtomicBoolean isViewActive, View view) {
        mValues = items;
        shouldSchedulePatients = isViewActive;
        itemHolders = new TreeMap<>();
        schedulePatients(view);
    }

    public void schedulePatients(View view) {
        new Thread(() -> {
            SleepUtil.sleep(5); //initial wait so the view holders get created

            while (shouldSchedulePatients.get()) {
                if (mValues.isEmpty()) {
                    shouldSchedulePatients.set(false);
                    return;
                }
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                PlaceholderItem patient = mValues.get(0);
                if (patient.isBeingChecked) {
                    ViewHolder patientViewHolder = itemHolders.get(patient.id);
                    if (patientViewHolder == null) {
                        continue;
                    }
                    activity.runOnUiThread(() -> removeItem(patientViewHolder));
                    SleepUtil.sleep(5);
                    continue;
                }
                ViewHolder patientViewHolder = itemHolders.get(patient.id);
                if (patientViewHolder == null) {
                    continue;
                }
                activity.runOnUiThread(() -> patientViewHolder.mContentView.setBackgroundColor(Color.YELLOW));
                patient.isBeingChecked = true;

                SleepUtil.sleep(5);
            }
        }).start();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false), shouldSchedulePatients);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.id);
        holder.mContentView.setText(holder.mItem.content);
        itemHolders.put(holder.mItem.id, holder);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void removeItem(ViewHolder holder) {
        int actualPosition = holder.getBindingAdapterPosition();
        mValues.remove(0);
        notifyItemRemoved(actualPosition);
        notifyItemRangeChanged(actualPosition, mValues.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public PlaceholderItem mItem;
        private final AtomicBoolean isViewActive;

        public ViewHolder(FragmentItemBinding binding, AtomicBoolean isViewActive) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            this.isViewActive = isViewActive;
            setClickListener();
        }

        private void setClickListener() {
            mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItem.isBeingChecked) {
                        Snackbar.make(view, "patient is currently being checked", Snackbar.LENGTH_LONG)
                                .show();
                        return;
                    }
                    Bundle b = new Bundle();
                    b.putString("patient_id", mItem.id);
                    b.putString("patient_content", mItem.content);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .replace(R.id.fragment_container_view, PatientInfoFragment.class, b)
                            .commit();

                    ViewHolder.this.isViewActive.set(false);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}