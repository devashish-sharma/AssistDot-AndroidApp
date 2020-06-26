package piyushjohnson.assistdot;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SubtaskDAO {

    @Query("SELECT * FROM Subtask")
    List<Subtask> getAllSubtasks();

    @Query("SELECT * FROM Subtask WHERE Id = :id")
    Subtask getSubtask(long id);

    @Query("SELECT * FROM Subtask WHERE Tid = :tid")
    List<Subtask> getSubtasksOfTask(long tid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllSubtasks(List<Subtask> subtasks);

    @Delete
    void DeleteAllSubtasks(Subtask... subtasks);

    @Update
    int UpdateAllSubtasks(List<Subtask> subtasks);
}
