package ru.varfolomeev.retail_test.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.varfolomeev.retail_test.model.Product;
import ru.varfolomeev.retail_test.repository.ProductRepository;
import ru.varfolomeev.retail_test.utils.ProductsMapper;
import ru.varfolomeev.retail_test.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductsMapper, FileOperations {

    private final ProductRepository productRepository;

    public Product getProduct(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found"));
    }

    public Collection<Product> getAllPrices() {
        return productRepository.findAll();
    }

    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public Boolean deleteProduct(Long id) {
        var productFromDb = productRepository.findById(id);
        if (productFromDb.isEmpty()) throw new RuntimeException(String.format("Product with id - %d - doesn`t exist", id));
        productRepository.delete(id);
        return true;
    }

    @Transactional
    public Product updatePrice(Product product) {
        var productFromDb = productRepository.findById(product.getId());
        if (productFromDb.isPresent()) {
            var updatedProduct = productFromDb.get();
            updatedProduct.setCategoryCode(product.getCategoryCode());
            updatedProduct.setCode(product.getCode());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setCategoryName(product.getCategoryName());
            productRepository.update(updatedProduct);
        }
        return product;
    }

    @Override
    public List<Product> mapDataToObj(Map<Integer, List<String>> data) {
        var products = new ArrayList<Product>();
        var keys = data.keySet();
        for (int i = 1; i < keys.size() - 1; i++) {
            List<String> row = data.get(i);
            products.add(
                    Product.builder()
                            .code(Long.valueOf(row.get(0)))
                            .description(row.get(1))
                            .categoryCode(row.get(2))
                            .categoryName(row.get(3))
                            .build()
            );
        }
        return products;
    }

    @Override
    public void saveDataFromExcel(MultipartFile file, String sheetName) throws IOException, InvalidFormatException {
        var data = Utils.readFromExcel(file, sheetName);
        var products = mapDataToObj(data);
        products.forEach(this::saveProduct);
    }
}
