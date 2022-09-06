package org.tu.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientInfoFragment extends Fragment {

    private String patientId;
    private String patientContent;

    public PatientInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getString("patient_id");
            patientContent = getArguments().getString("patient_content");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView fName = view.findViewById(R.id.first_name_text);
        fName.setText(patientContent); //split content string and display its components
        TextView lName = view.findViewById(R.id.last_name_text);
        lName.setText("alabala");
        TextView age = view.findViewById(R.id.age_text);
        age.setText("25");
        TextView symptom = view.findViewById(R.id.symptom_text);
        symptom.setText("cough");
        EditText insurance = view.findViewById(R.id.medical_insurance_field);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                Editable insuranceText = insurance.getText();
                String text = insuranceText.toString();

                if (text.matches("BG\\d{9}")) {
                    b.putString("patient_to_remove", patientId);
                } else {
                    Snackbar.make(view, "invalid insurance number", Snackbar.LENGTH_LONG)
                            .show();
                }

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.fragment_container_view, PatientFragment.class, b)
                        .commit();
            }
        });
    }

}