package com.example.user.paperflyv0;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListview extends ArrayAdapter<String> {

    String[] merchantName;
    String[] merchantAddress;
    Integer[] imgid;
    String[] scheduleTime;
    //String[] statusChange;
    private Activity context;
    public CustomListview(Activity context, String[] merchantName) {
        super(context, R.layout.listview_layout,merchantName);

        this.context=context;
        this.merchantName=merchantName;
        this.merchantAddress=merchantAddress;
        this.imgid=imgid;
        this.scheduleTime=scheduleTime;
       // this.statusChange=statusChange;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       View r=convertView;
       ViewHolder viewHolder=null;
       if (r==null)

       {
           LayoutInflater layoutInflater=context.getLayoutInflater();
           r=layoutInflater.inflate(R.layout.listview_layout,null,true);
           viewHolder=new ViewHolder(r);
           r.setTag(viewHolder);
       }
else
    {
        viewHolder=(ViewHolder)r.getTag();
    }
viewHolder.imgvw.setImageResource(imgid[position]);
       viewHolder.txt1.setText(merchantName[position]);
        viewHolder.txt2.setText(merchantAddress[position]);
        viewHolder.txt3.setText(scheduleTime[position]);
       // viewHolder.txt4.setText(statusChange[position]);
        return r;




    }
    class ViewHolder{

        TextView txt1;
        TextView txt2;
        TextView txt3;
       // TextView txt4;
        ImageView imgvw;
        ViewHolder(View v)
        {
            txt1=(TextView) v.findViewById(R.id.name);
            txt2=(TextView) v.findViewById(R.id.add);
            imgvw=v.findViewById(R.id.iconpf);
            txt3=(TextView) v.findViewById(R.id.ptime);
          //  txt4=(TextView) v.findViewById(R.id.chng_status);


        }


    }
}
