package neotech_back;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyRestController {

    private RatesController rates;
//    private final String origin = "http://localhost:63343";
    private final String origin = "*";
    private Statuses status = Statuses.LOADING;
    private Long satoshiDeposit = 90000000L;
    private Bet bet = new Bet();
    private Result result;

    MyRestController(){
        rates = new RatesController();
        rates.start();
        status = Statuses.READY;
    }

    public void setStatus(Statuses status){
        this.status = status;
    }

    @CrossOrigin(origins = origin)
    @RequestMapping("status")
    public StatusGetter statusGetter(){
        return new StatusGetter(status);
    }

    @CrossOrigin(origins = origin)
    @RequestMapping("deposit")
    public Long depositGetter(){
        return satoshiDeposit;
    }

    @CrossOrigin(origins = origin)
    @RequestMapping("rates_data")
    public RatesGetter ratesGetter(){
        return new RatesGetter(rates);
    }

    @CrossOrigin(origins = origin)
    @RequestMapping("result")
    public ResultGetter resultGetter(){
        return new ResultGetter(result);
    }

    @CrossOrigin(origins = origin)
    @RequestMapping("continue")
    public String continueSetter(){
        if(status == Statuses.RESULT){
            status = Statuses.READY;
            return "OK";
        }
        else{
            return "ERROR";
        }
    }

    @CrossOrigin(origins = origin)
    @RequestMapping("do_bet")
    public BetResult betChecker(@RequestParam(value="currency", defaultValue = "none") String currency, @RequestParam(value = "bet", defaultValue = "none") String betType, @RequestParam(value = "bet_value", defaultValue = "0") String betValue) {

        if(status != Statuses.READY ){
            return new BetResultError("Not ready");
        }

        List<String> currencyList = rates.getCurrencyList();
        if(!currencyList.contains(currency)){
            return new BetResultError("Wrong currency type");
        }

        if(!"up".equals(betType) && !"down".equals(betType)){
            return new BetResultError("Wrong bet type");
        }

        Long lBetValue;
        try{
            lBetValue = (long)(Float.parseFloat(betValue)*100000000);
        }
        catch (Exception e){
            e.printStackTrace();
            return new BetResultError("Wrong betValue");
        }

        if(lBetValue <= 0){
            return new BetResultError("Incorrect betValue");
        }

        if(satoshiDeposit - lBetValue < 0){
            return new BetResultError("Not enough BTC");
        }

        bet.setType(betType);
        bet.setValue(lBetValue);
        bet.setСurrency(currency);

        status = Statuses.WAITING;
        rates.getRateChange(currency, new Callback() {
            @Override
            public void callingBack(Float oldRate, Float newRate) {
                Float difference = newRate - oldRate;
                Boolean success = true;
                if(difference > 0 && bet.getType().equals("up")){
                    success = true;
                }
                else if(difference > 0 && bet.getType().equals("down")){
                    success = false;
                }
                else if(difference < 0 && bet.getType().equals("up")){
                    success = false;
                }
                else if(difference < 0 && bet.getType().equals("down")){
                    success = true;
                }

                if(success){
                    satoshiDeposit+=bet.getValue();
                }
                else{
                    satoshiDeposit-=bet.getValue();
                }

                result = new Result();
                result.setCurrency(bet.getСurrency());
                result.setSeccess(success);
                result.setValue((float)bet.getValue()/100000000);
                result.setСurrencyValue(newRate * result.getValue());

                status = Statuses.RESULT;
            }
        });

        return new BetResultOk();
    }

}
