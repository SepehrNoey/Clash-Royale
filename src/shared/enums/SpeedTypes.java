package shared.enums;

public enum SpeedTypes {
    SLOW(1), // numbers should change later!!
    MEDIUM(2),
    FAST(3),
    VERY_FAST(4); // is used for spells


    private final int value;

    private SpeedTypes(int value)
    {
        this.value = value;
    }

    /**
     * getter for value of each enum
     * @return value of each enum
     */
    public int getValue() {
        return value;
    }
}
