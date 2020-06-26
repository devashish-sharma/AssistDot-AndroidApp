package piyushjohnson.assistdot;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;



public class TaskListAdapter extends RecyclerView.Adapter {

    private Context context;
    private HashMap<Task,ArrayList<Subtask>> task_map;
    private ArrayList<Task> tasks;
    private ArrayList<Subtask> subtasks;
    private ArrayList<String> strings;
    private Task task;
    private TaskListListener listener;


    private static SwipeDismissBehavior getSwipeDismissBehaviour()
    {
        SwipeDismissBehavior swipeDismissBehavior = new SwipeDismissBehavior();
        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        return swipeDismissBehavior;
    }

    public static abstract class TaskListListener implements OnRecyclerItemListener
    {

    }

    public TaskListAdapter(Context context,HashMap<Task,ArrayList<Subtask>> task_map, ArrayList<Task> tasks,TaskListListener taskListListener) {
        this.context = context;
        this.task_map = task_map;
        this.tasks = tasks;
        this.listener = taskListListener;
    }

    private class SingleTask extends RecyclerView.ViewHolder
    {
        CheckBox checkBox;
        TextView textView;
        TextView textView2, reminderText;
        Button button;

        SingleTask(View view)
        {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
            textView = view.findViewById(R.id.textView);
            textView2 = view.findViewById(R.id.textView2);
            reminderText = view.findViewById(R.id.reminderText);
            button = view.findViewById(R.id.tagLine);
        }
    }

    private class MultiTask extends RecyclerView.ViewHolder
    {
        CheckBox checkBox;
        TextView textView;
        TextView textView2, reminderText;
        ListView listView;
        ImageButton imageButton;
        Button button;
        CardView cardView;
        ExpandableLayout expandableLayout;

        MultiTask(View view)
        {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
            cardView = view.findViewById(R.id.multi_task_cardView);
            textView = view.findViewById(R.id.textView);
            textView2 = view.findViewById(R.id.textView2);
            reminderText = view.findViewById(R.id.reminderText);
            listView = view.findViewById(R.id.listView);
            imageButton = view.findViewById(R.id.toggleButton);
            expandableLayout = view.findViewById(R.id.expandLayout);
            button = view.findViewById(R.id.tagLine);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("App:",task_map.size() + "");
        Log.d("App:",position + "");
        this.task = tasks.get(position);
        this.subtasks = new ArrayList<>();
        this.subtasks = this.task_map.get(task);
        if(subtasks.size() > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch(viewType)
        {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.single_task,parent,false);
                return new SingleTask(view);
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.multi_task,parent,false);
                return new MultiTask(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        this.task = tasks.get(position);
        this.subtasks = new ArrayList<>();
        this.strings = new ArrayList<>();
        this.subtasks = this.task_map.get(task);
        for(Subtask subtask:subtasks)
        {
            strings.add(subtask.getName());
//            Log.d("App:",subtask.getName());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(tasks.get(position),position);
            }
        });
        /*holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            private float x1,x2;
            static final int MIN_DISTANCE = 600;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {
//                                Toast.makeText(context, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                            }

                            // Right to left swipe action
                            else
                            {
//                                Toast.makeText(context, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                            }

                        }
                        else
                        {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                listener.onItemSwipe(tasks.get(position),position);
                return true;
            }
        });*/
        switch (holder.getItemViewType())
        {
            case 0:
                SingleTask singleTask = ((SingleTask) holder);
                singleTask.textView.setText(task.getName());
                singleTask.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        listener.onItemCheck(tasks.get(position),position);
                    }
                });
                setTagColor(singleTask.button,task.getTag());
                setStateOfTasks(singleTask.textView2, task.getState());
                setDate(singleTask.textView2,task.getDue_date()); //// TODO: working in progress by prem
                setReminderTime(singleTask.reminderText,task.getReminder());
                ///TODO: Reset Task State when User press checkbox.


                Log.d("App:",task.getDue_date() + " is Date From DB");
                break;
            case 1:
                final MultiTask multiTask = ((MultiTask) holder);
                multiTask.textView.setText(task.getName());
                multiTask.checkBox.isChecked();
                //TODO : Add custom layout to listview
                multiTask.listView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,strings));
                multiTask.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        multiTask.expandableLayout.toggle();
                    }
                });
                Log.d("App:",task.getTag() + " ");
                setTagColor(multiTask.button,task.getTag());
                setStateOfTasks(multiTask.textView2, task.getState());
                setDate(multiTask.textView2,task.getDue_date());
                setReminderTime(multiTask.reminderText,task.getReminder());
                break;
            default:
                break;
        }
    }

    private void setTagColor(View view,String color)
    {
        int c = Color.rgb(255,255,255);
        if(color != null)
        {
            switch (color)
            {
                case "Blue":
                    c = Color.rgb(30,136,229);
                    break;
                case "Red":
                    c = Color.rgb(229,57,53);
                    break;
                case "Green":
                    c = Color.rgb(67,160,71);
                    break;
                case "Yellow":
                    c = Color.rgb(253,216,53);
                    break;
                case "Purple":
                    c = Color.rgb(88,18,131);
                    break;
                case "Orange":
                    c = Color.rgb(255,112,67);
                    break;
            }
        }
        view.setBackgroundColor(c);
    }
    // this method set value at Tasks (Activity Home)
    private void setDate(View view,String date)
    {
        String dateFromDB = new Task().getDue_date();
        TextView textView = ((TextView) view);
        if (TextUtils.isEmpty(date) || date.compareTo("") == 0 ||date == dateFromDB) {
            textView.setText("Today");
        }
        else{
            textView.setText(date); //TODO: set dueDate in Task view
        }


    }

    private void setReminderTime(View view,String reminderTime){
        ((TextView) view).setText(reminderTime);
    }

    private void setStateOfTasks(View view, String State){
        ((TextView)view).setText(State);
    }


    @Override
    public int getItemCount() {
        return task_map.size();
    }

    public boolean isEmpty() {
        if(task_map.isEmpty())
        {
            return true;
        }
        return false;
    }
}
