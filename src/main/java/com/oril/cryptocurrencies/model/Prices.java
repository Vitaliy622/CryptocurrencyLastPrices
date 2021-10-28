package com.oril.cryptocurrencies.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Prices")
@NoArgsConstructor
@Getter
@Setter
public class Prices {
    private int lPrice;
    private String curr1;
    private String curr2;

    public Prices(int lPrice, String curr1, String curr2) {
        this.lPrice = lPrice;
        this.curr1 = curr1;
        this.curr2 = curr2;
    }
}
