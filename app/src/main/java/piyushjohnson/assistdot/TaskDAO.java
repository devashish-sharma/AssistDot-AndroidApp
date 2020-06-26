package piyushjohnson.assistdot;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM Task")
    List<Task> getAllTasks();

    @Query("SELECT COUNT(*) FROM Task")
    int getTotalTasks();

    @Query("SELECT * FROM Task WHERE name = :name")
    List<Task> searchTaskByName(String name);

    @Query("SELECT * FROM Task WHERE state = :state")
    List<Task> searchTaskByState(String state);

    @Query("SELECT * FROM Task WHERE tag = :tag")
    List<Task> searchTaskByTag(String tag);

    @Query("SELECT * FROM Task WHERE category = :category")
    List<Task> searchTaskByCategory(String category);

    @Query("select * from task where ( tag = :tag and state = :state ) or ( tag = :tag and due_date = :due_date ) or ( tag = :tag and reminder = :reminder ) or ( tag = :tag and category = :category )")
    List<Task> advancedPairSearch(String tag,String state,String due_date,String reminder,String category);

    @Query("select * from task where (tag = :tag) or (state = :state) or (due_date = :due_date) or (category = :category) or (reminder = :reminder) or (name = :name)")
    List<Task> advancedSearch(String tag,String state,String due_date,String category,String reminder,String name);

    @Query("SELECT * FROM Task WHERE Id = :id")
    Task getTask(long id);

    @Insert
    long[] insertAllTasks(Task... tasks);

    @Delete
    void DeleteAllTasks(Task... tasks);

    @Update
    int UpdateAllTasks(Task... tasks);
}
