package com.example.donationmanager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;


public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private EditText editCharityName, editFirstName, editLastName, editAddress, editCity, editPostcode, editState, editPhoneNumber;
    private int openHour, closeHour;
    private CheckBox mon, tue, wed, thu, fri, sat, sun;
    private ProgressDialog progressDialog;
    private Button buttonSave;
    private String accountType;
    private TextView tvOpenHours, tvCloseHours, tvOpenDays;
    //initialise db reference
    private DatabaseReference databaseReference, databaseReference1;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth fbAuth;
    int accountPosition;
    Spinner spinner1, spinner2, spinner3, spinner4;

    boolean mondayOpen;
    boolean tuesdayOpen;
    boolean wednesdayOpen;
    boolean thursdayOpen;
    boolean fridayOpen;
    boolean saturdayOpen;
    boolean sundayOpen;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());




        if(firebaseAuth !=null){
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Fragment fragment = null;
                        fragment = new ManageProfileFragment();
                        replaceFragment(fragment);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        progressDialog = new ProgressDialog(getContext());
        //initialise user information fields
        tvOpenHours = (TextView) v.findViewById(R.id.tvOpenHours);
        tvCloseHours = (TextView) v.findViewById(R.id.tvCloseHours);
        tvOpenDays = (TextView) v.findViewById(R.id.tvDays);

        buttonSave = (Button) v.findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(this);
        editFirstName = (EditText) v.findViewById(R.id.editFirstName);
        editLastName = (EditText) v.findViewById(R.id.editLastName);
        editAddress = (EditText) v.findViewById(R.id.editAddress);
        editCharityName = (EditText) v.findViewById(R.id.editCharityName);
        editCity = (EditText) v.findViewById(R.id.editCity);
        editPostcode = (EditText) v.findViewById(R.id.editPostcode);
        editPhoneNumber = (EditText) v.findViewById(R.id.editUserPhone);

        //Initialise and setup account selector spinner
        spinner1 = (Spinner) v.findViewById(R.id.accountTypeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(v.getContext(),R.array.accountType,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        //initialise days checkboxes

        mon = (CheckBox) v.findViewById(R.id.checkbox_monday);
        mon.setOnClickListener(this);
        tue = (CheckBox) v.findViewById(R.id.checkbox_tuesday);
        tue.setOnClickListener(this);
        wed = (CheckBox) v.findViewById(R.id.checkbox_wednesday);
        wed.setOnClickListener(this);
        thu = (CheckBox) v.findViewById(R.id.checkbox_thursday);
        thu.setOnClickListener(this);
        fri = (CheckBox) v.findViewById(R.id.checkbox_friday);
        fri.setOnClickListener(this);
        sat = (CheckBox) v.findViewById(R.id.checkbox_saturday);
        sat.setOnClickListener(this);
        sun = (CheckBox) v.findViewById(R.id.checkbox_sunday);
        sun.setOnClickListener(this);




        //initialise and setup state spinner
        spinner2 = (Spinner) v.findViewById(R.id.stateTypeSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(v.getContext(),R.array.states,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        //initialise opening hours spinner

        spinner3 = (Spinner) v.findViewById(R.id.openHourSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(v.getContext(),R.array.hours,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);

        //initialise closing hours spinner

        spinner4 = (Spinner) v.findViewById(R.id.closeHourSpinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(v.getContext(),R.array.hours,android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter3);
        spinner4.setOnItemSelectedListener(this);




        //hide or show fields based on account type


        accountPosition = spinner1.getSelectedItemPosition() +1;
        /*if(spinner1.getSelectedItemPosition() + 1 == 1) {
            editCharityName.setVisibility(View.GONE);
            tvOpenDays.setVisibility(View.GONE);
            tvCloseHours.setVisibility(View.GONE);
            tvOpenHours.setVisibility(View.GONE);
            spinner4.setVisibility(View.GONE);
            spinner3.setVisibility(View.GONE);
            mon.setVisibility(View.INVISIBLE);
            tue.setVisibility(View.INVISIBLE);
            wed.setVisibility(View.INVISIBLE);
            thu.setVisibility(View.INVISIBLE);
            fri.setVisibility(View.INVISIBLE);
            sat.setVisibility(View.INVISIBLE);
            sun.setVisibility(View.INVISIBLE);






        }
        else {
            editFirstName.setVisibility(v.GONE);
            editLastName.setVisibility(v.GONE);
        }*/
        return v;
    }



    private void saveUserInfo() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String postcode = editPostcode.getText().toString().trim();
        String state = spinner2.getSelectedItem().toString();
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        accountType = "Donor";
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uId = user.getUid();

        DonorInformation donorInformation = new DonorInformation(firstName, lastName, address,city, postcode, state, phoneNumber, accountType, uId, true);


        progressDialog.setMessage("Saving your Profile Information...");
        progressDialog.show();
        databaseReference.child("users").child(uId).setValue(donorInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 2000); // 2000 milliseconds delay
            }
        });

    }

    private void saveCharityInfo() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String charityName = editCharityName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String postcode = editPostcode.getText().toString().trim();
        String state = spinner2.getSelectedItem().toString();
        accountType = "Charity";
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        String uId = user.getUid();
        int openingHour = Integer.parseInt(spinner3.getSelectedItem().toString());
        int closingHour = Integer.parseInt(spinner4.getSelectedItem().toString());

        CharityInformation charityInformation = new CharityInformation(charityName, address, city, postcode, state, phoneNumber, accountType, uId, openingHour, closingHour, mondayOpen, tuesdayOpen, wednesdayOpen, thursdayOpen, fridayOpen, saturdayOpen, sundayOpen, true);

        databaseReference.child("users").child(uId).setValue(charityInformation);
        Toast.makeText(getContext(), "Charity information saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getSelectedItemPosition() == 0) {
            editCharityName.setVisibility(View.GONE);
            editFirstName.setVisibility(View.VISIBLE);
            editLastName.setVisibility(View.VISIBLE);
            tvOpenDays.setVisibility(View.GONE);
            tvCloseHours.setVisibility(View.GONE);
            tvOpenHours.setVisibility(View.GONE);
            mon.setVisibility(getView().GONE);
            tue.setVisibility(getView().GONE);
            wed.setVisibility(getView().GONE);
            thu.setVisibility(getView().GONE);
            fri.setVisibility(getView().GONE);
            sat.setVisibility(getView().GONE);
            sun.setVisibility(getView().GONE);

            spinner4.setVisibility(View.GONE);
            spinner3.setVisibility(View.GONE);
        }
        else if (parent.getSelectedItemPosition() == 1) {
            editFirstName.setVisibility(View.GONE);
            editLastName.setVisibility(View.GONE);
            editCharityName.setVisibility(View.VISIBLE);
            tvOpenDays.setVisibility(View.VISIBLE);
            tvCloseHours.setVisibility(View.VISIBLE);
            tvOpenHours.setVisibility(View.VISIBLE);
            mon.setVisibility(getView().VISIBLE);
            tue.setVisibility(getView().VISIBLE);
            wed.setVisibility(getView().VISIBLE);
            thu.setVisibility(getView().VISIBLE);
            fri.setVisibility(getView().VISIBLE);
            sat.setVisibility(getView().VISIBLE);
            sun.setVisibility(getView().VISIBLE);

            spinner4.setVisibility(View.VISIBLE);
            spinner3.setVisibility(View.VISIBLE);
        }

        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v == mon ) {

            if(mon.isChecked()) {
                mondayOpen = true;
            } else {
                mondayOpen = false;
            }
        }

        if(v == tue ) {

            if(tue.isChecked()) {
                tuesdayOpen = true;
            } else {
                tuesdayOpen = false;
            }
        }

        if(v == wed ) {

            if(wed.isChecked()) {
                wednesdayOpen = true;
            }
            else {
                wednesdayOpen = false;
            }
        }
        if(v == thu ) {

            if(thu.isChecked()) {
                thursdayOpen = true;
            } else {
                thursdayOpen = false;
            }
        }
        if(v == fri ) {

            if(fri.isChecked()) {
                fridayOpen = true;
            }
            else {
                fridayOpen = false;
            }
        }
        if(v == sat ) {

            if(sat.isChecked()) {
                saturdayOpen = true;
            } else {
                saturdayOpen = false;
            }
        }
        if(v == sun ) {

            if(sun.isChecked()) {
                sundayOpen = true;
            } else {
                sundayOpen = false;
            }
        }

        if(v == buttonSave) {

            if(spinner1.getSelectedItemPosition() == 0) {
                if (editFirstName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your first name", Toast.LENGTH_SHORT).show();
                } else if (editLastName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your last name", Toast.LENGTH_SHORT).show();
                } else if (editAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                } else if (editCity.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a city", Toast.LENGTH_SHORT).show();
                } else if (editPostcode.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a postcode", Toast.LENGTH_SHORT).show();
                } else if (editPhoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                } else {
                    saveUserInfo();
                    Fragment fragment = null;
                    fragment = new BookingFragment();
                    replaceFragment(fragment);
                }
            }
            else if(spinner1.getSelectedItemPosition() == 1) {
                if (editCharityName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter the name of your charity", Toast.LENGTH_SHORT).show();
                } else if (editAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                } else if (editCity.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a city", Toast.LENGTH_SHORT).show();
                } else if (editPostcode.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a postcode", Toast.LENGTH_SHORT).show();
                } else if (editPhoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                } else if (!mon.isChecked() & !tue.isChecked() & !wed.isChecked() & !thu.isChecked() & !fri.isChecked() & !sat.isChecked() & !sun.isChecked()) {
                    Toast.makeText(getContext(), "Please select days you are open", Toast.LENGTH_SHORT).show();
                } else if (spinner3.getSelectedItem().toString() == spinner4.getSelectedItem().toString() | spinner3.getSelectedItemPosition() > spinner4.getSelectedItemPosition()) {
                    Toast.makeText(getContext(), "Please setup your opening and closing times", Toast.LENGTH_SHORT).show();
                } else {
                    saveCharityInfo();
                    Fragment fragment = null;
                    fragment = new BookingFragment();
                    replaceFragment(fragment);
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
