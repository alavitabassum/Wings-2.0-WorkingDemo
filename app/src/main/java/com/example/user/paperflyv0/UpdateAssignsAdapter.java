package com.example.user.paperflyv0;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UpdateAssignsAdapter extends RecyclerView.Adapter<UpdateAssignsAdapter.ViewHolder> {

    private List<UpdateAssign_Model> updateAssignModelList;
    private Context context;
    private OnEditTextChanged onEditTextChanged;
    Database database;
    String merchantcode;

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
            itemExe = (AutoCompleteTextView)itemView.findViewById(R.id.auto_complete);
            itemCount = (TextView)itemView.findViewById(R.id.order_count);
            button = (Button) itemView.findViewById(R.id.update_assigns);

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

        viewHolder.itemCount.addTextChangedListener(new TextWatcher() {
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
                        Toast.makeText(context, "exu name is" + updateAssign_model.getEx_name() ,Toast.LENGTH_SHORT).show();
                        database.updateassign(merchantcode,cou);
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
}
