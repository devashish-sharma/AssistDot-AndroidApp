package piyushjohnson.assistdot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Piyush Johnson on 4/10/2018.
 */

public class StackAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private Context context;

    public StackAdapter(Context context,ArrayList<String> list)
    {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
     if(view == null)
     {
         view = inflater.inflate(R.layout.single_task,parent,false);
         holder = new ViewHolder(view);
         view.setTag(holder);
     }
     else
     {
         holder = (ViewHolder) view.getTag();
     }

     holder.textView.setText(list.get(position));
     holder.checkBox.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Toast.makeText(context,"added",Toast.LENGTH_SHORT).show();
         }
     });

        return view;
    }

    public class ViewHolder{
        TextView textView;
        CheckBox checkBox;
        public ViewHolder(View view)
        {
            textView = (TextView) view.findViewById(R.id.textView);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}
