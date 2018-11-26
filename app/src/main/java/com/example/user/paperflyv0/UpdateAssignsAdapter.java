package com.example.user.paperflyv0;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateAssignsAdapter extends RecyclerView.Adapter<UpdateAssignsAdapter.ViewHolder> {

    private List<UpdateAssign_Model> updateAssignModelList;
    private Context context;
    private OnEditTextChanged onEditTextChanged;

    Database database;
    String merchantcode;
    List<AssignManager_ExecutiveList> executiveLists;

    public interface OnEditTextChanged {
        void onTextChanged(int position, String charSeq);
    }

    public UpdateAssignsAdapter(List<UpdateAssign_Model> updateAssignModelList, Context context,String merchantcode,OnEditTextChanged onEditTextChanged) {
        this.updateAssignModelList = updateAssignModelList;
        this.context = context;
        this.merchantcode = merchantcode;
        this.onEditTextChanged = onEditTextChanged;

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public AutoCompleteTextView itemExe;
        public TextView itemCount;
        public Button button;


        public ViewHolder(View itemView) {
            super(itemView);
            database = new Database(context);
            executiveLists = new ArrayList<>();
            getallexecutives();
            itemExe = (AutoCompleteTextView)itemView.findViewById(R.id.auto_complete);
            itemCount = (TextView)itemView.findViewById(R.id.order_count);
            button = (Button) itemView.findViewById(R.id.update_assigns);

            itemExe.setDropDownBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.black)));
            itemExe.setTextColor(itemView.getResources().getColor(R.color.black));
            }
    }

    @Override
    public UpdateAssignsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.update_assigns_layout, viewGroup, false);
        UpdateAssignsAdapter.ViewHolder viewHolder = new UpdateAssignsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UpdateAssignsAdapter.ViewHolder viewHolder, final int j) {
        final UpdateAssign_Model updateAssign_model = updateAssignModelList.get(j);
        viewHolder.itemExe.setText(updateAssign_model.getEx_name());
        viewHolder.itemCount.setText(updateAssign_model.getCount());

        /*autocomplete*/

        List<String> lables = new ArrayList<String>();

            for (int z = 0; z < executiveLists.size(); z++) {
                lables.add(executiveLists.get(z).getExecutive_name());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1, lables);
                   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.itemExe.setAdapter(adapter);
        viewHolder.itemExe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{onEditTextChanged.onTextChanged(j, charSequence.toString());}catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*final String beforeempname = viewHolder.itemExe.getText().toString();
                final String beforeempcode = database.getSelectedEmployeeCode(beforeempname);*/
                final String cou = viewHolder.itemCount.getText().toString();
                final String empname = editable.toString();
                final String empcode = database.getSelectedEmployeeCode(empname);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String rowid = updateAssign_model.getRowid();
                        database.updateassign(rowid,empname,empcode,cou);
                        Toast.makeText(context, "updated" ,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        viewHolder.itemCount.addTextChangedListener(new TextWatcher() {
            final String empname = viewHolder.itemExe.getText().toString();
            final String empcode = database.getSelectedEmployeeCode(empname);

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{onEditTextChanged.onTextChanged(j, charSequence.toString());}catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String cou = editable.toString();
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String rowid = updateAssign_model.getRowid();
                        database.updateassign(rowid,empname,empcode,cou);
                        assignexecutiveupdate(merchantcode,empcode,cou);
                        Toast.makeText(context, "updated" ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        }



    @Override
    public int getItemCount() {
        return updateAssignModelList.size();
    }

    private void assignexecutiveupdate(final String merchantcode, final String empcode,final String cou) {


        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://192.168.0.111/new/updateassign.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //   Log.d("Error",error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchant_code", merchantcode);
                params.put("executive_code", empcode);
                params.put("order_count", cou);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(postRequest);

    }

    private void getallexecutives() {
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_executivelist(sqLiteDatabase);
            while (c.moveToNext()) {
                String empName = c.getString(0);
                String empCode = c.getString(1);
                AssignManager_ExecutiveList assignManager_executiveList = new AssignManager_ExecutiveList(empName, empCode);
                executiveLists.add(assignManager_executiveList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
