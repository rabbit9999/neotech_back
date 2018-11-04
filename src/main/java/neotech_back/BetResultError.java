package neotech_back;

import java.util.HashMap;
import java.util.Map;

public class BetResultError implements BetResult {

    private String message = "";

    BetResultError(String message){
        this.message = message;
    }

    public String getError(){
        return this.message;
    }
    public Map<String,String> getData(){
        return new HashMap<>();
    }
}
