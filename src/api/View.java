package api;

import java.util.Observer;
import java.util.Observable;

public abstract class View implements Observer {
    public abstract void render(Game game);

    /**
     * Convenience method for the update implementation required by
     * the Observer interface.
     **/
    public void update(Observable o) {
        update(o, null);
    }
}
