package neotech_back;

import java.util.HashMap;
import java.util.Map;

public class BetResultOk implements BetResult {
    public String getError(){
        return null;
    }
    public Map<String,String> getData(){
        Map<String,String> result = new HashMap<>();
        result.put("result","ok");
        return result;
    }
}
