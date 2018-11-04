package neotech_back;

import java.util.List;
import java.util.Map;

public class RatesHistoryGetter {
    private RatesController rates = null;

    RatesHistoryGetter(RatesController rates){
        this.rates = rates;
    }

    public Map<Long, Map<String, Float>> getRates_history(){
        return rates.getHistory();
    }
}
