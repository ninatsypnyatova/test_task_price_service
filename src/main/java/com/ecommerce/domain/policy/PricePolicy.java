package com.ecommerce.domain.policy;

import com.ecommerce.domain.model.Price;

import java.util.Comparator;

/**
 * Domain policy that encodes business rules for {@link Price} selection.
 *
 * <p>This class has no Spring annotations; it belongs purely to the domain layer and
 * expresses business decisions explicitly in code.</p>
 */
public class PricePolicy {

    /**
     * Returns a {@link Comparator} that orders prices so the one with the highest
     * priority comes first.
     *
     * <p>When two tariffs overlap in time, the one with the greater {@code priority}
     * value is the commercially applicable tariff.</p>
     *
     * @return comparator for descending priority order
     */
    public Comparator<Price> highestPriorityFirst() {
        return Comparator.comparing(Price::priority).reversed();
    }
}
