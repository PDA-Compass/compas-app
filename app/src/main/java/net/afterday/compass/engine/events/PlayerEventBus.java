package net.afterday.compass.engine.events;

import net.afterday.compass.core.player.Player;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by spaka on 5/3/2018.
 */

public class PlayerEventBus
{
    public static final String SUICIDE = "suicide";

    private static PlayerEventBus instance;
    private static final Subject<Integer> playerLevelChanges = PublishSubject.create();
    private static final Subject<Integer> playerInfluences = PublishSubject.create();
    private static final Subject<String> playerCommands = PublishSubject.create();

    private PlayerEventBus()
    {

    }

    public static PlayerEventBus instance()
    {
        if(instance == null)
        {
            instance = new PlayerEventBus();
        }
        return instance;
    }

    public void suicide()
    {
        playerCommands.onNext(SUICIDE);
    }

    public Observable<String> getPlayerCommandsStream()
    {
        return playerCommands;
    }
}
