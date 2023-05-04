package ru.varfolomeev.retail_test.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.varfolomeev.retail_test.model.Price;
import ru.varfolomeev.retail_test.repository.PriceRepository;
import ru.varfolomeev.retail_test.utils.PriceMapper;
import ru.varfolomeev.retail_test.utils.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PriceService implements PriceMapper, FileOperations {

    private final PriceRepository priceRepository;
    private final ProductService productService;
    private final ChainService chainService;

    public Price getPrice(Long id) {
        return priceRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found"));
    }

    public Collection<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @Transactional
    public void savePrice(Price price) {
        priceRepository.save(price);
    }

    @Transactional
    public Boolean deletePrice(Long id) {
        var priceFromDb = priceRepository.findById(id);
        if (priceFromDb.isEmpty()) throw new RuntimeException(String.format("Price with id - %d - doesn`t exist", id));
        priceRepository.delete(id);
        return true;
    }

    @Transactional
    public Price updatePrice(Price price) {
        var priceFromDb = priceRepository.findById(price.getId());
        if (priceFromDb.isPresent()) {
            var updatedPrice = priceFromDb.get();
            updatedPrice.setChain(price.getChain());
            updatedPrice.setRegularPrice(price.getRegularPrice());
            updatedPrice.setProduct(price.getProduct());
            priceRepository.update(updatedPrice);
        }
        return price;
    }

    @Override
    public List<Price> mapDataToObj(Map<Integer,List<String>> data) {
        var prices = new ArrayList<Price>();
        var keys = data.keySet();
        for (int i = 1; i < keys.size() - 1; i++) {
            List<String> row = data.get(i);
            prices.add(
                    Price.builder()
                            .chain(chainService.getChain(Long.valueOf(row.get(0))))
                            .product(productService.getProduct(Long.parseLong(row.get(1))))
                            .regularPrice(new BigDecimal(row.get(2)))
                            .build()
            );
        }
        return prices;
    }

    @Override
    public void saveDataFromExcel(MultipartFile file, String sheetName) throws IOException, InvalidFormatException {
        var data = Utils.readFromExcel(file, sheetName);
        var prices = mapDataToObj(data);
        prices.forEach(this::savePrice);
    }
}
