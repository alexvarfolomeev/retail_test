package ru.varfolomeev.retail_test.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.varfolomeev.retail_test.model.Chain;
import ru.varfolomeev.retail_test.repository.impl.ChainRepositoryImpl;
import ru.varfolomeev.retail_test.utils.ChainMapper;
import ru.varfolomeev.retail_test.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChainService implements ChainMapper, FileOperations {

    private final ChainRepositoryImpl chainRepository;

    public Chain getChain(Long id) {
        return chainRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Chain not found"));
    }

    public Collection<Chain> getAllChains() {
        return chainRepository.findAll();
    }

    @Transactional
    public void saveChain(Chain chain) {
        chainRepository.save(chain);
    }

    @Transactional
    public Boolean deleteChain(Long id) {
        var chainFromDb = chainRepository.findById(id);
        if (chainFromDb.isEmpty()) throw new RuntimeException(String.format("Chain with id - %d - doesn`t exist", id));
        chainRepository.delete(id);
        return true;
    }

    @Transactional
    public Chain updateCustomer(Chain chain) {
        var chainFromDb = chainRepository.findById(chain.getId());
        var updatedChain = new Chain();
        if (chainFromDb.isPresent()) {
            updatedChain = chainFromDb.get();
            updatedChain.setChainName(chain.getChainName());
            chainRepository.update(updatedChain);
        } else {
            throw new RuntimeException("Customer doesn`t exist");
        }
        return updatedChain;
    }

    @Override
    public void saveDataFromExcel(MultipartFile file, String sheetName) throws IOException, InvalidFormatException {
        var data = Utils.readFromExcel(file, sheetName);
        var customers = mapDataToObj(data);
        customers.forEach(this::saveChain);
    }

    @Override
    public List<Chain> mapDataToObj(Map<Integer, List<String>> data) {
        var chains = new ArrayList<Chain>();
        var keys = data.keySet();
        for (int i = 1; i < keys.size() - 1; i++) {
            List<String> row = data.get(i);
            chains.add(
                    Chain.builder()
                            .chainName(row.get(0))
                            .build()
            );
        }
        return chains;
    }
}
