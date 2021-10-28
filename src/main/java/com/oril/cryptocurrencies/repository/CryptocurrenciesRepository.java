package com.oril.cryptocurrencies.repository;

import com.oril.cryptocurrencies.model.Prices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptocurrenciesRepository extends MongoRepository<Prices, Long> {

    List<Prices> findAllByCurr1(String currName);

    List<Prices> findAllByCurr1(String currName, PageRequest pageRequest);
}
