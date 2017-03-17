package the31st.my_pharmacy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static the31st.my_pharmacy.R.id.drEmail;
import static the31st.my_pharmacy.R.id.drName;
import static the31st.my_pharmacy.R.id.kinEmail;
import static the31st.my_pharmacy.R.id.kinName;
import static the31st.my_pharmacy.R.id.kinNumber;

/**
 * Created by TIH on 3/15/2017.
 */

public class MainLandActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView mPrescription, mSickness, mStageSickness, mDrName, mDrEmail, mKinName, mKinEmail, mKinNumber, mAddress;
    private ImageButton mSend, mSearch;


    private ImageView mImage;

    private FirebaseAuth mAuth;

    String pres, patient_sick, stage, dr_Name, dr_Email, adress, kin_Name, kin_Email, kin_Number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_land);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Patient_Details");

        mPrescription = (TextView) findViewById(R.id.presc);
        mSickness = (TextView) findViewById(R.id.sick);
        mStageSickness = (TextView) findViewById(R.id.sickStage);
        mDrName = (TextView) findViewById(drName);
        mDrEmail = (TextView) findViewById(drEmail);
        mKinName = (TextView) findViewById(kinName);
        mKinEmail = (TextView) findViewById(kinEmail);
        mKinNumber = (TextView) findViewById(kinNumber);
        mAddress = (TextView) findViewById(R.id.adres);
        mImage = (ImageView) findViewById(R.id.profile_image);
        mSend = (ImageButton) findViewById(R.id.fbSend);
        mSearch = (ImageButton) findViewById(R.id.fbSearch);


        Picasso.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(mImage);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainLandActivity.this, MapsActivity.class));
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+"sk.manana@gmail.com")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Patient Prescription");
                intent.putExtra(Intent.EXTRA_TEXT, "Greetings Pharmacy\n\n"+"Patient Prescription: " + pres + "\nSick Type: " + patient_sick + "\nSick Stage: " + stage + "\nAddress: " + adress
                        + "\nDoctor's Name: " + dr_Name + "\nDoctor's Email: " + dr_Email + "\nNext of Kin Name: " + kin_Name + "\nNext of Kin Email: " + kin_Email
                        + "\nNext of Kin Number: " + kin_Number + "\n\nKind Regards" +"\n"+ mAuth.getCurrentUser().getDisplayName());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        readDetails();

    }

    public void readDetails() {

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String pres = (String) dataSnapshot.child("patient_name").getValue();
//                String patient_sick = (String) dataSnapshot.child("patient_sickness").getValue();
//                String stage = (String) dataSnapshot.child("patient_stage_sickness").getValue();
//                String drName = (String) dataSnapshot.child("patient_dr_name").getValue();
//                String drEmail = (String) dataSnapshot.child("patient_dr_email").getValue();
//                String adress = (String) dataSnapshot.child("patient_address").getValue();
//                String kinName = (String) dataSnapshot.child("kin_name").getValue();
//                String kinEmail = (String) dataSnapshot.child("kin_email").getValue();
//                String kinNumber = (String) dataSnapshot.child("kin_number").getValue();

        pres = getIntent().getStringExtra("prescription");
        patient_sick = getIntent().getStringExtra("sickness");
        stage = getIntent().getStringExtra("stage_sickness");
        dr_Name = getIntent().getStringExtra("dr_name");
        dr_Email = getIntent().getStringExtra("dr_email");
        adress = getIntent().getStringExtra("address");
        kin_Name = getIntent().getStringExtra("kin_name");
        kin_Email = getIntent().getStringExtra("kin_email");
        kin_Number = getIntent().getStringExtra("kin_number");


        mPrescription.setText(pres);
        mSickness.setText(patient_sick);
        mStageSickness.setText(stage);
        mDrName.setText(dr_Name);
        mDrEmail.setText(dr_Email);
        mAddress.setText(adress);
        mKinName.setText(kin_Name);
        mKinEmail.setText(kin_Email);
        mKinNumber.setText(kin_Number);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }


}
