package piyushjohnson.assistdot;

import android.support.design.widget.SwipeDismissBehavior;

import java.io.Serializable;

public interface OnRecyclerItemListener {
    void onItemClick(Object item,int pos);
    void onItemCheck(Object item,int pos);
}
