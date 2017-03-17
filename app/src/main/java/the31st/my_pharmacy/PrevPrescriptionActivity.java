package the31st.my_pharmacy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrevPrescriptionActivity extends AppCompatActivity {

    Button mNewPresc,mExit;

    private DatabaseReference mDatabase;

    FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_prescription);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Patient_Details");

        mNewPresc = (Button) findViewById(R.id.button_newPresc);

        firebaseAuth = FirebaseAuth.getInstance();

        mExit = (Button) findViewById(R.id.exit);

        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();

                startActivity(new Intent(getApplicationContext(),login.class));

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewPresc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Registration.class));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Details, PatientDetailsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Details, PatientDetailsViewHolder>(
                Details.class,
                R.layout.custom_row,
                PatientDetailsViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(PatientDetailsViewHolder viewHolder, Details model, int position) {

                final String key = getRef(position).getKey();


                viewHolder.setPatient_sickness(model.patient_sickness);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent viewIntent = new Intent(getApplicationContext(), ViewPrescription.class);
                        viewIntent.putExtra("patient_key", key);
                        startActivity(viewIntent);

                    }
                });

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class PatientDetailsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public PatientDetailsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setPatient_sickness(String patient_sickness) {
            TextView tvSick = (TextView) mView.findViewById(R.id.sic_name);
            tvSick.setText(patient_sickness);
        }

    }

}
