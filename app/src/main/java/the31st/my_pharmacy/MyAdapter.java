package the31st.my_pharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by TIH on 3/13/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<List_Item> listItems;
    private Context context;
    String pres, patient_sick, stage, dr_Name, dr_Email, adress, kin_Name, kin_Email, kin_Number;

    FirebaseAuth mAuth;

    public MyAdapter(List<List_Item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_row, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
//                holder.textViewHead.;
//            }
//        });

        holder.textViewHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if item still exists
                if (position != RecyclerView.NO_POSITION) {
                    List_Item clickedDataItem = listItems.get(position);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("pharmacy_place_name", clickedDataItem.getHead()); //InputString: from the EditText
                    editor.commit();


                    Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getHead(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"+"sk.manana@gmail.com")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Patient Prescription");
                    intent.putExtra(Intent.EXTRA_TEXT, "Greetings Pharmacy\n\n" + clickedDataItem.getHead());
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }

//                    Intent intent = new Intent(getApplicationContext(), );
//                    intent.putExtra("place_name", clickedDataItem.getHead());
//                    context.startActivity(intent);


                }

            }
        });

        List_Item list_item = listItems.get(position);

        holder.textViewHead.setText(list_item.getHead());
        holder.textViewDesc.setText(list_item.getDesc());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        public TextView textViewHead, textViewDesc;
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            textViewHead = (TextView) itemView.findViewById(R.id.tvHeader);
            textViewDesc = (TextView) itemView.findViewById(R.id.tvSubHeader);

        }


    }


}
