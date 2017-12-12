package diana.montyhall;

public enum SwitchStrategy {

    ALWAYS_SWITCH("Всегда меняем"),
    NO_SWITCH("Не меняем свой выбор"),
    SWITCH_ON_LAST("Меняем на последнем шаге");

    private final String strategy;

    SwitchStrategy(String strategy)
    {
        this.strategy = strategy;
    }

    public String getStrategy()
    {
        return strategy;
    }
}
