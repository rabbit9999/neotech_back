package neotech_back;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RatesController {

    private String ratesDataUrl = "https://api.coindesk.com/v1/bpi/currentprice.json?t=123454";
    private Long lastUpdateTime = 0L;
    private JsonObject rateData = null;
    private TreeMap<Long,JsonObject> history = new TreeMap<>();
    private Callback rateChangeCallback = null;
    private String targetCurrency = "";


    public void start(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try{
                    URL url = new URL(ratesDataUrl);
                    Scanner s = new Scanner(url.openStream());
                    JsonParser parser = new JsonParser();
                    JsonObject mainObject = parser.parse(s.nextLine()).getAsJsonObject();

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                    String dateString = mainObject.getAsJsonObject("time").get("updatedISO").getAsString();
                    Date parsedDate =  df.parse(dateString);
                    lastUpdateTime = parsedDate.getTime();
                    rateData = mainObject.getAsJsonObject("bpi");
                    checkTargetCurrencyChange(rateData);
                    if(history.size() == 0 || history.lastKey() < lastUpdateTime){
                        history.put(lastUpdateTime,rateData);
//                        System.out.println("Add in history");
                    }

                    //Remove history older than one hour
                    if(history.firstKey() < lastUpdateTime - (1000*60*60)){
                        history.remove(history.firstKey());
//                        System.out.println("Remove from history");
                    }
                }
                catch(IOException|ParseException ex) {
                    ex.printStackTrace();
                }
            }
        };

        Timer timer = new Timer("RatesUpdateTimer");
        timer.scheduleAtFixedRate(timerTask, 30, 15000);
    }

    public Map<String,Float> getCurrentRates(){
        Map<String,Float> res = new HashMap<>();
        for(String currency:rateData.keySet()){
            res.put(currency,rateData.getAsJsonObject(currency).get("rate_float").getAsFloat());
        }
        return res;
    }

    public Map<String,String> getCurrencySymbols(){
        Map<String,String> res = new HashMap<>();
        for(String currency:rateData.keySet()){
            res.put(currency,rateData.getAsJsonObject(currency).get("symbol").getAsString());
        }
        return res;
    }

    public Long getLastUpdateTime(){
        return lastUpdateTime;
    }

    public Map<Long,Map<String,Float>> getHistory(){
        Map<Long, Map<String,Float>> res = new TreeMap<>();

        for(Map.Entry<Long,JsonObject> entry : history.entrySet()){
            Map<String,Float> item = new HashMap<>();
            for(String currency:entry.getValue().keySet()){
                item.put(currency,entry.getValue().getAsJsonObject(currency).get("rate_float").getAsFloat());
            }
            res.put(entry.getKey(),item);
        }
        return res;
    }

    public List<String> getCurrencyList(){
        return new ArrayList<>(rateData.keySet());
    }

    private void checkTargetCurrencyChange(JsonObject rateData){

        if(rateData.keySet().contains(targetCurrency)){
            Float newRate = rateData.getAsJsonObject(targetCurrency).get("rate_float").getAsFloat();
            Float oldRate = history.lastEntry().getValue().getAsJsonObject(targetCurrency).get("rate_float").getAsFloat();
            if(!oldRate.equals(newRate) && rateChangeCallback != null){
                rateChangeCallback.callingBack(oldRate,newRate);
                rateChangeCallback = null;
                targetCurrency = "";
            }
        }
    }

    public void getRateChange(String currency, Callback callback){
        targetCurrency = currency;
        rateChangeCallback = callback;
    }

}
