package com.oril.cryptocurrencies.service;

import com.oril.cryptocurrencies.model.Prices;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CryptocurrenciesService {

    Prices findCurrByMaxPrice(String currName);

    Prices findCurrByMinPrice(String currName);

    List<Prices> findAllByCurrName(String name, int pageNum, int pageSize);

    void getCsvReport(HttpServletResponse response) throws IOException;
}
