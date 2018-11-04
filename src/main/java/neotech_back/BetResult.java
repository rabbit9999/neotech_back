package neotech_back;

import java.util.Map;

public interface BetResult {

    String getError();
    Map<String,String> getData();
}
