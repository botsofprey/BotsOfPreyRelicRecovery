package Actions;

/**
 * Created by Jeremy on 8/26/2017.
 */

public interface ActionHandler {
    public boolean doAction(String action, long maxTimeAllowed); //do one action and wait until done.
    public boolean stopAction(String action); //stopNavigation the thread of an action
    public boolean startDoingAction(String action); //thread off a particular action
    public void stop();
}