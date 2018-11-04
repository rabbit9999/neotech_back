package neotech_back;

import java.util.List;
import java.util.Map;

public class RatesGetter {

    private RatesController rates = null;

    RatesGetter(RatesController rates){
        this.rates = rates;
    }

    public Map<String, Float> getCurrent_rates_data(){
        return rates.getCurrentRates();
    }

    public Map<String, String> getCurrency_symbols(){
        return rates.getCurrencySymbols();
    }

    public Long getLast_update_time(){
        return rates.getLastUpdateTime();
    }

    public Map<Long,Map<String, Float>> getRates_history(){
        return rates.getHistory();
    }

}
