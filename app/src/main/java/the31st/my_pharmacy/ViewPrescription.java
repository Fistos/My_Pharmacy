package the31st.my_pharmacy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewPrescription extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView mPrescription, mSickness, mStageSickness, mDrName, mDrEmail, mKinName, mKinEmail, mKinNumber, mAddress;
    private ImageView mImage;
    private ImageButton mSearch;
    private Button mSend;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.from_text_link);

        String patient_key = getIntent().getStringExtra("patient_key");

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Patient_Details");

        mPrescription = (TextView) findViewById(R.id.tv_presc);
        mSickness = (TextView) findViewById(R.id.tv_sick);
        mStageSickness = (TextView) findViewById(R.id.tv_sickStage);
        mDrName = (TextView) findViewById(R.id.tv_drName);
        mDrEmail = (TextView) findViewById(R.id.tv_drEmail);
        mKinName = (TextView) findViewById(R.id.tv_kinName);
        mKinEmail = (TextView) findViewById(R.id.tv_kinEmail);
        mKinNumber = (TextView) findViewById(R.id.tv_kinNumber);
        mAddress = (TextView) findViewById(R.id.tv_adres);
        mImage = (ImageView) findViewById(R.id.imgv_user_image);
        mSend = (Button) findViewById(R.id.imgbSend);

        mDatabase.child(patient_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String pres = (String) dataSnapshot.child("prescription").getValue();
                final String sick = (String) dataSnapshot.child("patient_sickness").getValue();
                final String stage = (String) dataSnapshot.child("patient_stage_sickness").getValue();
                final String name_dr = (String) dataSnapshot.child("patient_dr_name").getValue();
                final String email_dr = (String) dataSnapshot.child("patient_dr_email").getValue();
                final String address_patient = (String) dataSnapshot.child("patient_address").getValue();
                final String name_kin = (String) dataSnapshot.child("kin_name").getValue();
                final String email_kin = (String) dataSnapshot.child("kin_email").getValue();
                final String number_kin = (String) dataSnapshot.child("kin_number").getValue();

                mPrescription.setText(pres);
                mSickness.setText(sick);
                mStageSickness.setText(stage);
                mDrName.setText(name_dr);
                mDrEmail.setText(email_dr);
                mKinName.setText(name_kin);
                mKinEmail.setText(email_kin);
                mKinNumber.setText(number_kin);
                mAddress.setText(address_patient);

                Picasso.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(mImage);


                mSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + "sk.manana@gmail.com")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Patient Prescription");
                        intent.putExtra(Intent.EXTRA_TEXT, "Greetings Pharmacy\n\n" + "As I have previously requested the below medicine before, I would like to request it again.\n\n"
                                + "Patient Prescription: " + pres + "\nSick Type: " + sick + "\nSick Stage: " + stage + "\nAddress: " + address_patient
                                + "\nDoctor's Name: " + name_dr + "\nDoctor's Email: " + email_dr + "\nNext of Kin Name: " + name_kin + "\nNext of Kin Email: " + email_kin
                                + "\nNext of Kin Number: " + number_kin + "\n\nKind Regards" + "\n" + mAuth.getCurrentUser().getDisplayName());
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Something went wrong ...", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
