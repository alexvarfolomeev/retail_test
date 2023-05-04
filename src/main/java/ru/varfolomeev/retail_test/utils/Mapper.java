package ru.varfolomeev.retail_test.utils;

import java.util.List;
import java.util.Map;

public interface Mapper<T> {
    List<T> mapDataToObj(Map<Integer, List<String>>data);
}
