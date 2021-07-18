package shared.enums;

public enum SpeedTypes {
    SLOW(1), // numbers should change later!!
    MEDIUM(2),
    FAST(3),
    VERY_FAST(4); // is used for spells

    private double value;

    private SpeedTypes(double value)
    {
        this.value = value;
    }

    /**
     * getter for value of each enum
     * @return value of each enum
     */
    public double getValue() {
        return value;
    }

    public void setValue(double newSpeed)
    {
        this.value = newSpeed;
    }

}
