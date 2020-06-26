package piyushjohnson.assistdot;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by samuelraj on 15/04/18.
 */

@Entity(tableName = "Task",indices = @Index("Id"))
public class Task implements Serializable{


    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "name")
    private String Name;

    @ColumnInfo(name = "state")
    private String State;

    @ColumnInfo(name = "due_date")
    private String Due_date;

    @ColumnInfo(name = "category")
    private String Category;

    @ColumnInfo(name = "tag")
    private String Tag;

    @ColumnInfo(name = "reminder")
    private String Reminder;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDue_date() {
        return Due_date;
    }

    public void setDue_date(String due_date) {
        Due_date = due_date;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getReminder() {
        return Reminder;
    }

    public void setReminder(String reminder) {
        Reminder = reminder;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

}
