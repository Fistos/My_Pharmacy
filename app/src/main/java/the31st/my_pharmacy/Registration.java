package the31st.my_pharmacy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static the31st.my_pharmacy.R.id.kin_name;

public class Registration extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mSickness, mStageSickness, mDrName, mDrEmail, mAddress, mPrescription, mKinName, mKinNumber, mKinEmail;
    private Button mPreview;
    String user_uid;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mSickness = (EditText) findViewById(R.id.sickness);
        mStageSickness = (EditText) findViewById(R.id.stage_sickness);
        mDrName = (EditText) findViewById(R.id.dr_name);
        mDrEmail = (EditText) findViewById(R.id.dr_email);
        mAddress = (EditText) findViewById(R.id.address);
        mPrescription = (EditText) findViewById(R.id.prescription);
        mKinName = (EditText) findViewById(kin_name);
        mKinEmail = (EditText) findViewById(R.id.kin_email);
        mKinNumber = (EditText) findViewById(R.id.kin_number);
        mPreview = (Button) findViewById(R.id.btnPreview);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        mCollapsingToolbarLayout.setTitle("Registration");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Patient_Details");
        mAuth = FirebaseAuth.getInstance();

        user_uid = mAuth.getCurrentUser().getUid();

        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent detailsIntent = new Intent(Registration.this, MainLandActivity.class);
                detailsIntent.putExtra("sickness", mSickness.getText().toString());
                detailsIntent.putExtra("stage_sickness", mStageSickness.getText().toString());
                detailsIntent.putExtra("dr_name", mDrName.getText().toString());
                detailsIntent.putExtra("dr_email", mDrEmail.getText().toString());
                detailsIntent.putExtra("address", mAddress.getText().toString());
                detailsIntent.putExtra("prescription", mPrescription.getText().toString());
                detailsIntent.putExtra("kin_name", mKinName.getText().toString());
                detailsIntent.putExtra("kin_email", mKinEmail.getText().toString());
                detailsIntent.putExtra("kin_number", mKinNumber.getText().toString());

                startActivity(detailsIntent);

                sendPatientInfo();

            }
        });

    }

    public void sendPatientInfo() {

        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("send details ...");
        mProgress.show();

        final String sickness = mSickness.getText().toString().trim();
        final String stage_sickness = mStageSickness.getText().toString().trim();
        final String dr_name = mDrName.getText().toString().trim();
        final String dr_email = mDrEmail.getText().toString().trim();
        final String address = mAddress.getText().toString().trim();
        final String prescription = mPrescription.getText().toString().trim();
        final String kin_name = mKinName.getText().toString().trim();
        final String kin_email = mKinEmail.getText().toString().trim();
        final String kin_number = mKinNumber.getText().toString().trim();

        if (!TextUtils.isEmpty(sickness) && !TextUtils.isEmpty(stage_sickness) && !TextUtils.isEmpty(dr_name)
                && !TextUtils.isEmpty(dr_email) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(prescription) && !TextUtils.isEmpty(kin_name)
                && !TextUtils.isEmpty(kin_email) && !TextUtils.isEmpty(kin_number)) {


            mDatabase.child(user_uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    DatabaseReference newPatient = mDatabase.push();
                    newPatient.child("prescription").setValue(prescription);
                    newPatient.child("patient_name").setValue(mAuth.getCurrentUser().getDisplayName());
                    newPatient.child("patient_sickness").setValue(sickness);
                    newPatient.child("patient_stage_sickness").setValue(stage_sickness);
                    newPatient.child("patient_dr_name").setValue(dr_name);
                    newPatient.child("patient_dr_email").setValue(dr_email);
                    newPatient.child("patient_address").setValue(address);
                    newPatient.child("kin_name").setValue(kin_name);
                    newPatient.child("kin_email").setValue(kin_email);
                    newPatient.child("kin_number").setValue(kin_number);

                    mProgress.dismiss();

                    Toast.makeText(Registration.this, "details submitted." , Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(Registration.this, "Something went wrong ..." + databaseError , Toast.LENGTH_SHORT).show();

                }
            });

        }


    }


}
