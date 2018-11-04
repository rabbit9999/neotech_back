package neotech_back;

public class Result {
    public Float value;
    public Float сurrencyValue;
    public String currency;
    public Boolean seccess;

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getСurrencyValue() {
        return сurrencyValue;
    }

    public void setСurrencyValue(Float сurrencyValue) {
        this.сurrencyValue = сurrencyValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getSeccess() {
        return seccess;
    }

    public void setSeccess(Boolean seccess) {
        this.seccess = seccess;
    }
}
