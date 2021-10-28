package com.oril.cryptocurrencies.service;

import com.oril.cryptocurrencies.exceptions.NotFoundException;
import com.oril.cryptocurrencies.model.CsvReport;
import com.oril.cryptocurrencies.model.Prices;
import com.oril.cryptocurrencies.repository.CryptocurrenciesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class CryptocurrenciesServiceImpl implements CryptocurrenciesService {

    private final CryptocurrenciesRepository cryptocurrenciesRepository;

    public CryptocurrenciesServiceImpl(CryptocurrenciesRepository cryptocurrenciesRepository) {
        this.cryptocurrenciesRepository = cryptocurrenciesRepository;
    }

    @Override
    public Prices findCurrByMaxPrice(String currName) {
        List<Prices> pricesList = new ArrayList<>(cryptocurrenciesRepository.findAllByCurr1(currName));
        return pricesList.stream()
                .max(Comparator.comparing(Prices::getLPrice))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Prices findCurrByMinPrice(String currName) {
        List<Prices> pricesList = new ArrayList<>(cryptocurrenciesRepository.findAllByCurr1(currName));
        return pricesList.stream()
                .min(Comparator.comparing(Prices::getLPrice))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Prices> findAllByCurrName(String name, int pageNum, int pageSize) {
        return cryptocurrenciesRepository.findAllByCurr1(name,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "lPrice")));
    }

    @Override
    public void getCsvReport(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cryptocurrencies_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        CsvReport btc = getDataForCsvReport("BTC");
        CsvReport eth = getDataForCsvReport("ETH");
        CsvReport xrp = getDataForCsvReport("XRP");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Curr Name", "Max Price", "Min Price"};
        String[] nameMapping = {"currName", "maxPrice", "minPrice"};

        csvWriter.writeHeader(csvHeader);

        csvWriter.write(btc, nameMapping);
        csvWriter.write(eth, nameMapping);
        csvWriter.write(xrp, nameMapping);

        csvWriter.close();
    }

    public CsvReport getDataForCsvReport(String currName) {
        CsvReport csvReport = new CsvReport();
        csvReport.setCurrName(currName);
        csvReport.setMaxPrice(findCurrByMaxPrice(currName).getLPrice());
        csvReport.setMinPrice(findCurrByMinPrice(currName).getLPrice());

        return csvReport;
    }
}