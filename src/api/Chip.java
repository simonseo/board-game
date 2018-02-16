package api;

public enum Chip {
    EMPTY,
    RED,
    BLUE;

    public boolean isEmpty() {
        return equals(Chip.EMPTY);
    }
}
