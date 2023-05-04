package ru.varfolomeev.retail_test.repository;

import java.util.Collection;
import java.util.List;

public interface Analysis<T> {
    Collection<T> getAnalysisWithPromoSign();
    Collection<T> getAnalysisWithChainsAndProductsFilter(List<Integer> chains, List<Integer> products);
}
