package neotech_back;

public class ResultGetter {

    private Result result;

    ResultGetter(Result result){
        this.result = result;
    }

    public Boolean getSuccess(){
        return result.getSeccess();
    }

    public String getCurrency(){
        return result.getCurrency();
    }

    public Float getValue(){
        return result.getValue();
    }

    public Float getCurrency_value(){
        return result.getСurrencyValue();
    }
}
