package ru.varfolomeev.retail_test.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.varfolomeev.retail_test.model.Customer;
import ru.varfolomeev.retail_test.repository.impl.CustomerRepositoryImpl;
import ru.varfolomeev.retail_test.utils.CustomerMapper;
import ru.varfolomeev.retail_test.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerMapper, FileOperations {

    private final CustomerRepositoryImpl customerRepository;
    private final ChainService chainService;

    public Customer getCustomer(Long id) {
        return customerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found"));
    }

    public Collection<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public Boolean deleteCustomer(Long id) {
        var priceFromDb = customerRepository.findById(id);
        if (priceFromDb.isEmpty()) throw new RuntimeException(String.format("Shipment with id - %d - doesn`t exist", id));
        customerRepository.delete(id);
        return true;
    }

    @Transactional
    public Customer updateCustomer(Customer customer) {
        var customerFromDb = customerRepository.findById(customer.getId());
        if (customerFromDb.isPresent()) {
            var customerToUpdate = customerFromDb.get();
            customerToUpdate.setCode(customer.getCode());
            customerToUpdate.setName(customer.getName());
            customerToUpdate.setChain(customer.getChain());
            customerRepository.update(customerToUpdate);
        } else {
            throw new RuntimeException("Customer doesn`t exist in the database");
        }
        return customer;
    }

    @Override
    public void saveDataFromExcel(MultipartFile file, String sheetName) throws IOException, InvalidFormatException {
        var data = Utils.readFromExcel(file, sheetName);
        var customers = mapDataToObj(data);
        customers.forEach(this::saveCustomer);
    }

    @Override
    public List<Customer> mapDataToObj(Map<Integer, List<String>> data) {
        var products = new ArrayList<Customer>();
        var keys = data.keySet();
        for (int i = 1; i < keys.size() - 1; i++) {
            List<String> row = data.get(i);
            products.add(
                    Customer.builder()
                            .name(row.get(0))
                            .code(Integer.valueOf(row.get(1)))
                            .chain(chainService.getChain(Long.valueOf(row.get(2))))
                            .build()
            );
        }
        return products;
    }
}
