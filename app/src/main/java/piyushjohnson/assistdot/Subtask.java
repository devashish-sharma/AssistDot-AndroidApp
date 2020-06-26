package piyushjohnson.assistdot;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@Entity(tableName = "Subtask",foreignKeys = @ForeignKey(entity = Task.class,parentColumns = "Id",childColumns = "tid"),indices = @Index("tid"))
public class Subtask implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "tid")
    private int Tid;

    @ColumnInfo(name = "name")
    private String Name;

    @ColumnInfo(name = "state")
    private boolean State;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTid() {
        return Tid;
    }

    public void setTid(int tid) {
        Tid = tid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }

}
