package neotech_back;

import java.util.Map;

public class StatusGetter {
    private Statuses status;

    StatusGetter(Statuses status){
        this.status = status;
    }

    public String getStatus(){
        return status.toString();
    }

    public Map<String,String> getData(){
        return null;
    }
}
