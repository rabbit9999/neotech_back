package neotech_back;

public class Bet {
    public String type;
    public Long value;
    public String сurrency;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getСurrency() {
        return сurrency;
    }

    public void setСurrency(String сurrency) {
        this.сurrency = сurrency;
    }
}
