package com.oril.cryptocurrencies.controller;

import com.oril.cryptocurrencies.model.Prices;
import com.oril.cryptocurrencies.service.CryptocurrenciesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/cryptocurrencies")
public class CryptocurrenciesController {

    private final CryptocurrenciesService cryptocurrenciesService;

    public CryptocurrenciesController(CryptocurrenciesService cryptocurrenciesService) {
        this.cryptocurrenciesService = cryptocurrenciesService;
    }


    @GetMapping("/maxprice")
    public Prices getMaxPrice(@RequestParam(required = false, value = "name") String name) {
        return cryptocurrenciesService.findCurrByMaxPrice(name);
    }

    @GetMapping("/minprice")
    public Prices getMinPrice(@RequestParam(required = false, value = "name") String name) {
        return cryptocurrenciesService.findCurrByMinPrice(name);
    }

    @GetMapping
    public List<Prices> getSelectedPageOfPrices(@RequestParam(required = false, name = "name") String name,
                                                @RequestParam(required = false, name = "page_number") int pageNumber,
                                                @RequestParam(required = false, name = "page_size") int pageSize) {
        return cryptocurrenciesService.findAllByCurrName(name, pageNumber, pageSize);

    }

    @GetMapping("/csv")
    public void getCsvReport(HttpServletResponse response) throws IOException {
        cryptocurrenciesService.getCsvReport(response);
    }
}