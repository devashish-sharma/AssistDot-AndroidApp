package piyushjohnson.assistdot;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskList extends Fragment{

    ArrayList<Task> tasks;
    HashMap<Task,ArrayList<Subtask>> task_map;
    RecyclerView recyclerView;
    TextView textView;
    String Title;
    Task task;
    onTaskListEventListener listener;
//    TaskListAdapter.TaskListListener listener;

    public TaskList() {
        // Required empty public constructor
    }


    public static TaskList newInstance(HashMap<Task,ArrayList<Subtask>> task_map, ArrayList<Task> tasks, String Title, TaskListAdapter.TaskListListener listListener)  {
        TaskList fragment = new TaskList();
        Bundle bundle = new Bundle();
        bundle.putString("title",Title);
//        bundle.putSerializable("listener",listListener);
        bundle.putSerializable("tasks",tasks);
        bundle.putSerializable("task_map",task_map);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface onTaskListEventListener
    {
        void onItemClick(Object item, int pos);
//        void onItemCheck(Object item, int pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (onTaskListEventListener) context;
        }catch (ClassCastException e)
        {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        TaskListAdapter taskListAdapter = new TaskListAdapter(getActivity(), task_map, tasks, new TaskListAdapter.TaskListListener() {
            @Override
            public void onItemClick(Object item, int pos) {
                listener.onItemClick(item,pos);
            }

            @Override
            public void onItemCheck(Object item, int pos) {
//                listener.onItemCheck(item,pos);
                task = ((Task)item);
                task.setState("Done");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Home.db.taskDAO().UpdateAllTasks(task);
                    }
                }).start();
                Toast.makeText(getActivity(),task.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        /*if(taskListAdapter.isEmpty())
        {
            textView.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null)
        {
            tasks = (ArrayList<Task>) getArguments().getSerializable("tasks");
            task_map = (HashMap<Task, ArrayList<Subtask>>) getArguments().getSerializable("task_map");
            Title = getArguments().getString("title");
//            listener =(TaskListAdapter.TaskListListener) getArguments().getSerializable("listener");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = ((LinearLayout)view);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        textView = (TextView) layout.findViewById(R.id.textView);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Fragment:","Stopped" + Title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment:","Destroyed" + Title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Fragment:","Detached" + Title);
    }

    /*new TaskListAdapter.TaskListListener() {
        @Override
        public void onItemClick(Object item, int pos) {

        }

        @Override
        public void onItemCheck(Object item, int pos) {
            task = ((Task)item);
            task.setState("Done");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Home.db.taskDAO().UpdateAllTasks(task);
                }
            }).start();
            Toast.makeText(getActivity(),task.getName(),Toast.LENGTH_SHORT).show();

        }
    }
*/
}
