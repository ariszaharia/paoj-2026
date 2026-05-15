package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;

public final class CustomCollectors {

    private CustomCollectors() {}

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {

        // Clasa interna mutabila — vizibila doar in interiorul metodei
        class Agg {
            Map<String, Long> byCountry  = new HashMap<>();
            Map<String, Long> byChannel  = new HashMap<>();
            BigDecimal total             = BigDecimal.ZERO;
            List<Transaction> allTx      = new ArrayList<>();
        }

        return Collector.of(

                // 1. SUPPLIER — container gol
                Agg::new,

                // 2. ACCUMULATOR — o tranzactie intra in Agg
                (agg, tx) -> {
                    agg.byCountry.merge(tx.getCountry(), 1L, Long::sum);
                    agg.byChannel.merge(tx.getChannel(), 1L, Long::sum);
                    agg.total = agg.total.add(tx.getAmount());
                    agg.allTx.add(tx);
                },

                // 3. COMBINER — pentru parallelStream(), uneste doua Agg-uri
                (a, b) -> {
                    b.byCountry.forEach((k, v) -> a.byCountry.merge(k, v, Long::sum));
                    b.byChannel.forEach((k, v) -> a.byChannel.merge(k, v, Long::sum));
                    a.total = a.total.add(b.total);
                    a.allTx.addAll(b.allTx);
                    return a;
                },

                // 4. FINISHER — construieste Snapshot-ul imutabil
                agg -> {
                    List<Transaction> top = agg.allTx.stream()
                            .sorted(Comparator
                                    .comparing(Transaction::getAmount).reversed()
                                    .thenComparingInt(Transaction::getId)) // tie-breaker stabil
                            .limit(topN)
                            .toList();

                    return new Snapshot(agg.byCountry, agg.byChannel, agg.total, top);
                },

                Collector.Characteristics.UNORDERED
        );
    }
}