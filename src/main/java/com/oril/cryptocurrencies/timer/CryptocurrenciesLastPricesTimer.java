package com.oril.cryptocurrencies.timer;

import com.oril.cryptocurrencies.model.Prices;
import com.oril.cryptocurrencies.repository.CryptocurrenciesRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class CryptocurrenciesLastPricesTimer {

    private final CryptocurrenciesRepository cryptocurrenciesRepository;

    public CryptocurrenciesLastPricesTimer(CryptocurrenciesRepository cryptocurrenciesRepository) {
        this.cryptocurrenciesRepository = cryptocurrenciesRepository;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void getLastPricesBTC() throws JSONException {
        getLastPrices("BTC");
        getLastPrices("ETH");
        getLastPrices("XRP");
    }

    public void getLastPrices(String cryptocurrencyName) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-agent", "SomeUserAgent");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        String result = rt.exchange("https://cex.io/api/last_price/" + cryptocurrencyName + "/USD", HttpMethod.GET, entity, String.class).getBody();
        assert result != null;
        JSONObject jsonObject = new JSONObject(result);
        Prices prices = new Prices();
        prices.setLPrice(jsonObject.getInt("lprice"));
        prices.setCurr1(jsonObject.getString("curr1"));
        prices.setCurr2(jsonObject.getString("curr2"));
        cryptocurrenciesRepository.save(prices);
    }
}