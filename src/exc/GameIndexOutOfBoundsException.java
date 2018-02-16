package exc;

import java.lang.IndexOutOfBoundsException;

public class GameIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public GameIndexOutOfBoundsException(int row, int col) {
        super("(" + String.valueOf(row) + "," + String.valueOf(col) + ")");
    }
}
