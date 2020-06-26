package piyushjohnson.assistdot;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EditTask extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    EditText editText, editText2, editText3, editText4, editText5;
    TextView textView;
    RadioGroup radioGroup;
    Spinner spinner;
    View view;
    Task task;
    HashMap<Task, ArrayList<Subtask>> task_map;
    List<Task> tasks;
    List<Subtask> subtasks;
    ArrayList<Task> tasks_name;
    public static AppDatabase db;
    Calendar cal,calTime;
    Date time;
    String time1;
    String date;
    String category;
    String tag;
    Intent intent;
    public final static String EditTask = "EditTask";
    public final static String NewTask = "NewTask";
    String MODE;
    int savedId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        calTime = Calendar.getInstance();
        calTime.set(Calendar.HOUR,Calendar.getInstance().getTime().getHours());
        calTime.set(Calendar.MINUTE,Calendar.getInstance().getTime().getMinutes());
        calTime.set(Calendar.SECOND,Calendar.getInstance().getTime().getSeconds());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AssistDB").build();

        tasks = new ArrayList<>();
        subtasks = new ArrayList<>();
        task = new Task();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.spinner);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        textView = (TextView) findViewById(R.id.textView);

        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);

        editText4.setOnKeyListener(new View.OnKeyListener() {
            int val = 0;

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    new Tasker3().execute(editText4.getText().toString());
                    textView.append(editText4.getText().toString() + "\n");
                }
                return true;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = ((TextView) view).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio1:
                        tag = "Blue";
                        break;
                    case R.id.radio2:
                        tag = "Red";
                        break;
                    case R.id.radio3:
                        tag = "Green";
                        break;
                    case R.id.radio4:
                        tag = "Yellow";
                        break;
                    case R.id.radio5:
                        tag = "Purple";
                        break;
                    case R.id.radio6:
                        tag = "Orange";
                        break;
                    default:
                        tag = null;
                }
            }
        });

        intent = getIntent();
        if (intent.hasExtra(EditTask)) {
            if (intent.getBooleanExtra(EditTask, false)) {
                MODE = EditTask;
                Task task = (Task) intent.getExtras().getSerializable("task");
                ArrayList<Subtask> subtasks = (ArrayList<Subtask>) intent.getExtras().getSerializable("subtask");
                startEditMode(task, subtasks);
            }

        }
        if (intent.hasExtra(NewTask)) {
            if (intent.getBooleanExtra(NewTask, false)) {
                MODE = NewTask;
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.Save:
                switch (MODE) {
                    case EditTask:
                        Log.d("Task", task.getId() + "");
                        new Tasker().execute("EditTask");
                        item.setChecked(true);
                        break;
                    case NewTask:
                        new Tasker().execute("NewTask");
                        item.setChecked(true);
                        break;

                }
                finish();
                startActivity(new Intent(EditTask.this, Home.class));
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText2:
                final DatePickerDialog datePickerDialog = new DatePickerDialog(this) {
                    @Override
                    public void onDateChanged(@NonNull DatePicker view, int day, int month, int dayOfMonth) {
                        cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, view.getYear());
                        cal.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                        cal.set(Calendar.MONTH, view.getMonth());
                        String format = new SimpleDateFormat("E, MMM d").format(cal.getTime());
                        editText2.setText((format));
                        //calTime = cal; // copied for reminder time picker
                    }
                };
                datePickerDialog.show();
                break;
            case R.id.editText3:
                new TimePickerDialog(this, this, 1, 1, false) {
                }.show();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hh, int mm) {
        calTime.set(Calendar.HOUR,hh);
        calTime.set(Calendar.MINUTE,mm);
        calTime.set(Calendar.SECOND,00);
        calTime.set(Calendar.AM_PM, calTime.getTime().getHours());
        Log.d("Time:",calTime.getTime().getHours()+":"+calTime.getTime().getMinutes()+" "+ calTime.get(Calendar.AM_PM));
        date = calTime.getTime().getHours()+":"+calTime.getTime().getMinutes()+" "+ calTime.get(Calendar.AM_PM);
        if (0 == calTime.get(Calendar.AM)) {
            date = ((calTime.getTime().getHours() - 12) + ":" + calTime.getTime().getMinutes() + " AM");
        } else {
            date = ((calTime.getTime().getHours()) + ":" + calTime.getTime().getMinutes() + " PM");
        }
        try {
            DateFormat df = new SimpleDateFormat("hh:mm aa");
            time = df.parse(date);
            time1 = df.format(time);
            editText3.setText(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editText3.setText(time1);
    }

    public void startEditMode(Task task, ArrayList<Subtask> subtasks) {
        editText.setText(task.getName());
        editText2.setText(task.getDue_date());
        editText3.setText(task.getReminder());
        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                textView.append(subtask.getName() + "\n");
            }
        }
        spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition(task.getCategory()));
        this.task = task;
        this.subtasks = subtasks;
    }

    public String getTaskState() {
        String dueDate = editText2.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("E, MMM d");
        String currentDate = formatter.format(Calendar.getInstance().getTime());

        if (TextUtils.isEmpty(dueDate) || currentDate.compareTo(dueDate) == 0) {
            return "Today";
        }
        return "Upcoming";

        /* //Todo: Again Date distance find
        long due = cal.getTimeInMillis();
        long curr = Calendar.getInstance().getTimeInMillis();
        long distanceInMills = due - curr;
        long DistanceInDays = distanceInMills/(86400000);
        Log.d("MyDate:","Distance in Days is - "+(DistanceInDays+1));*/

    }

    public String getDueDate() {
        String dueDate = editText2.getText().toString();
        return dueDate;
    }

    public String getReminder() {
        String Reminder = editText3.getText().toString();
        if (TextUtils.isEmpty(Reminder) || Reminder.compareTo("") == 0) {
            return "No Reminder";
        }
        return Reminder;

    }

    public String getCategory() {
        return category;
    }

    private class Tasker extends AsyncTask<Object, String, Boolean> {
        TaskDAO taskDAO;
        SubtaskDAO subtaskDAO;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            task.setName(editText.getText().toString());
            task.setCategory(getCategory());
            task.setState(getTaskState());
            task.setDue_date(getDueDate());
            task.setReminder(getReminder());
            task.setTag(tag);
            taskDAO = db.taskDAO();
            subtaskDAO = db.subtaskDAO();

        }

        @Override
        protected void onPostExecute(Boolean bool) {

//            Toast.makeText(EditTask.this, "saved", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Boolean doInBackground(Object... action) {

            switch ((String) action[0]) {
                case "NewTask":
                    NewTask();
                    setReminder();
                    return true;
                case "EditTask":
                    EditTask();
                    return true;
                default:
                    return false;
            }
        }

        private void NewTask() {
            long id[] = taskDAO.insertAllTasks(task);
            for (Subtask subtask : subtasks) {
                subtask.setTid((int) id[0]);
            }
            subtaskDAO.insertAllSubtasks(subtasks);
        }

        private void EditTask() {
            int id = taskDAO.UpdateAllTasks(task);
            for (Subtask subtask : subtasks) {
                subtask.setTid(task.getId());
            }
            subtaskDAO.insertAllSubtasks(subtasks);
        }

        private void setReminder()
        {
            AlarmManager manager =(AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(EditTask.this,Reminder.class);
            intent.putExtra("alarm",task.getName());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1,intent,0);
            manager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
        }
    }


    private class Tasker3 extends AsyncTask<String, String, Boolean> {

        TaskDAO taskDAO;
        SubtaskDAO subtaskDAO;
        Subtask subtask;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskDAO = db.taskDAO();
            subtaskDAO = db.subtaskDAO();
            subtask = new Subtask();
        }

        @Override
        protected void onPostExecute(Boolean bool) {

        }


        @Override
        protected Boolean doInBackground(String... strings) {
            subtask.setTid(taskDAO.getTotalTasks() + 1);
            subtask.setName(strings[0]);
            subtask.setState(false);
            subtasks.add(subtask);
            return true;
        }
    }


}


