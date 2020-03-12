package net.afterday.compas.engine.actions;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by spaka on 7/28/2018.
 */

public class ActionsExecutor
{
    private static final String TAG = "ActionsExecutor";
    private static ActionsExecutor instance;
    private List<Action> actions = new ArrayList<>();
    private Action closestAction;


    private ActionsExecutor(Observable<Long> ticks)
    {
        ticks.subscribe((t) -> {
            if(closestAction != null && closestAction.startTime() <= System.currentTimeMillis())
            {
                Action action = closestAction;
                actions.remove(closestAction);
                closestAction = findClosestAction();
                action.execute();
            }
        });
    }

    public void postAction(Action action)
    {
        //Log.i(TAG, "Post action: " + action);
        actions.add(action);
        closestAction = findClosestAction();
    }

    public static ActionsExecutor instance()
    {
        if(instance == null)
        {
            throw new IllegalStateException("Actions executor must be initialized!");
        }
        return instance;
    }

    public static ActionsExecutor instance(Observable<Long> ticks)
    {
        if(instance == null)
        {
            instance = new ActionsExecutor(ticks);
        }
        return instance;
    }

    private Action findClosestAction()
    {
        //Log.e(TAG, "findClosestAction");
        Action ca = null;
        for(Action a : actions)
        {
            if(ca == null)
            {
                ca = a;
            }else if(a.startTime() < ca.startTime())
            {
                ca = a;
            }
            //Log.e(TAG, "Action: " + ca);
        }
        //Log.e(TAG, "Closest action: " + ca);
        return ca;
    }
}
