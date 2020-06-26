package piyushjohnson.assistdot;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity implements TaskList.onTaskListEventListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView UserProfile;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    public static AppDatabase db;
    Task task;
    HashMap<Task, ArrayList<Subtask>> task_map;
    HashMap<String, String> search_result;
    TaskListAdapter.TaskListListener listener;
    List<Task> tasks;
    List<Subtask> subtasks;
    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AssistDB").build();

        fragmentManager = getSupportFragmentManager();
        new Tasker2().execute("Read","Today");

        tasks = new ArrayList<>();
        subtasks = new ArrayList<>();
        search_result = new HashMap<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.botttomNavigation);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, EditTask.class);
                intent.putExtra(EditTask.NewTask,true);
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String Category = null;
                switch(item.getItemId())
                {
                    case R.id.home:
                        Category = "Home";
                        break;
                    case R.id.work:
                        Category = "Work";
                        break;
                    case R.id.office:
                        Category = "Office";
                        break;
                }
                new Tasker2().execute("Category",Category);
                drawerLayout.closeDrawers();
                item.setChecked(true);
                return true;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                String State = null;
                switch (item.getItemId()) {
                    case R.id.mytoday:
//                        Toast.makeText(Home.this, "mytoday", Toast.LENGTH_SHORT).show();
                        State = "Today";
                        new Tasker2().execute("Read",State);
                        return true;
                    case R.id.upcoming:
//                        Toast.makeText(Home.this, "upcoming", Toast.LENGTH_SHORT).show();
                        State = "Upcoming";
                        new Tasker2().execute("Read",State);
                        return true;
                    case R.id.done:
//                        Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                        State = "Done";
                        new Tasker2().execute("Read",State);
                        return true;
                    default:
                        return true;
                }
                /*fragment = TaskList.newInstance(task_map, (ArrayList<Task>) tasks,State);
                fragmentManager.beginTransaction().replace(R.id.listContainer,fragment).commit();*/
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                item.setChecked(true);
                return true;
            default:
                return false;
        }
    }

/*    @Override
    public void onBackPressed() {

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);

        searchViewItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                toolbar.setBackgroundColor(getColor(R.color.cardview_light_background));
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
                fragmentManager.popBackStack();
//                    fragmentManager.beginTransaction().replace(R.id.listContainer, fragmentManager.findFragmentByTag("tasklist")).commit();
                return true;
            }
        });
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        View mSearchEditFrame = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        mSearchEditFrame.setBackground(getDrawable(R.drawable.custom_edit_text));
        Log.d("App:",mSearchEditFrame.getClass().toString());
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    if (!query.matches("[#:!^@*.].*")) {
                        search_result.put("Keyword", query);
                    } else {
                        search_result = new SearchParser(query).parse();
                    }

//                    Log.d("App:",(String)search_result.get("Tag") + "");
                    new Tasker4().execute(search_result);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(Object item, int pos) {
        Intent i = new Intent(this,EditTask.class);
        Bundle bundle = new Bundle();
        Task task = ((Task)item);
        bundle.putSerializable("task",task);
        bundle.putSerializable("subtask",task_map.get(task));
        i.putExtra(EditTask.EditTask,true);
        i.putExtras(bundle);
        startActivity(i);
//        Toast.makeText(this,((Task)item).getName(),Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onItemCheck(Object item, int pos) {
        new Tasker2().execute("Update","Done",item);
        Toast.makeText(this,((Task)item).getState(),Toast.LENGTH_SHORT).show();
    }*/

    private class Tasker2 extends AsyncTask<Object, String, Boolean> {
        TaskDAO taskDAO;
        SubtaskDAO subtaskDAO;
        int mAction;
        String Action,State,Category;
        Task task;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            task = new Task();
            task_map = new HashMap<>();
            taskDAO = db.taskDAO();
            subtaskDAO = db.subtaskDAO();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            fragment = TaskList.newInstance(task_map, (ArrayList<Task>) tasks, State, listener);
            fragmentManager.beginTransaction().replace(R.id.listContainer, fragment, "tasklist").commit();
//            Toast.makeText(Home.this, "read", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Object... action) {

            switch ((String)action[0])
            {
                case "Read":
                    State = (String) action[1];
                    Read();
                    break;
                case "Update":
                    task = ((Task)action[2]);
                    Update();
                    break;
                case "Delete":
                    Delete();
                case "Category":
                    Category = (String) action[1];
                    Category();
                    break;
                    default:
                        break;
            }
            return true;
        }

        private void Read()
        {
            switch (State) {
                case "Today":
                    tasks = taskDAO.searchTaskByState(State);
                    break;
                case "Upcoming":
                    tasks = taskDAO.searchTaskByState(State);
                    break;
                case "Done":
                    tasks = taskDAO.searchTaskByState(State);
                    break;
                default:
                    tasks = taskDAO.getAllTasks();
                    break;
            }

            for (Task task : tasks) {
                subtasks = subtaskDAO.getSubtasksOfTask(task.getId());
                task_map.put(task, (ArrayList<Subtask>) subtasks);
            }
        }

        private void Update()
        {
            task.setState("Done");
            taskDAO.UpdateAllTasks(task);
        }
        private void Delete()
        {

        }
        private void Category()
        {
            tasks = taskDAO.searchTaskByCategory(Category);
            for (Task task : tasks) {
                subtasks = subtaskDAO.getSubtasksOfTask(task.getId());
                task_map.put(task, (ArrayList<Subtask>) subtasks);
            }
        }

    }

    private class Tasker4 extends AsyncTask<HashMap<String, String>, String, Boolean> {
        TaskDAO taskDAO;
        SubtaskDAO subtaskDAO;
        HashMap<String, String> search_map;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            task = new Task();
            taskDAO = db.taskDAO();
            subtaskDAO = db.subtaskDAO();
            task_map = new HashMap<>();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            fragment = TaskList.newInstance(task_map, (ArrayList<Task>) tasks, "Search", listener);
            fragmentManager.beginTransaction().replace(R.id.listContainer, fragment, "search_fragment").addToBackStack(null).commit();
//            Toast.makeText(Home.this, "read", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(HashMap<String, String>... s) {
            search_map = s[0];
            if (search_map.size() == 1) {
                tasks = taskDAO.advancedSearch(search_map.get("Tag"), search_map.get("State"), search_map.get("Date"),search_map.get("Category"), search_map.get("Reminder"), search_map.get("Keyword"));
            } else {
                tasks = taskDAO.advancedPairSearch(search_map.get("Tag"), search_map.get("State"), search_map.get("Date"), search_map.get("Reminder"),search_map.get("Category"));
            }

            for (Task task : tasks) {
                subtasks = subtaskDAO.getSubtasksOfTask(task.getId());
                task_map.put(task, (ArrayList<Subtask>) subtasks);
            }
            return true;
        }

    }


}