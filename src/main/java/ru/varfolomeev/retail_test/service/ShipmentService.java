package ru.varfolomeev.retail_test.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.varfolomeev.retail_test.model.*;
import ru.varfolomeev.retail_test.repository.PriceRepository;
import ru.varfolomeev.retail_test.repository.impl.ShipmentRepositoryImpl;
import ru.varfolomeev.retail_test.utils.ShipmentMapper;
import ru.varfolomeev.retail_test.utils.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static ru.varfolomeev.retail_test.model.PromoSign.PROMO;
import static ru.varfolomeev.retail_test.model.PromoSign.REGULAR;

@Service
@RequiredArgsConstructor
public class ShipmentService implements ShipmentMapper, FileOperations {

    private final ShipmentRepositoryImpl shipmentRepository;
    private final PriceRepository priceRepository;
    private final ProductService productService;
    private final CustomerService customerService;
    private final ChainService chainService;

    public Shipment getShipment(Long id) {
        return shipmentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
    }

    public Collection<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @Transactional
    public void saveShipment(Shipment shipment) {
        shipment.setPromoSign(isPromoSign(shipment) == 0 ? REGULAR : PROMO);
        shipmentRepository.save(shipment);
    }

    @Transactional
    public Boolean deleteShipment(Long id) {
        var shipmentFromDb = shipmentRepository.findById(id);
        if (shipmentFromDb.isEmpty()) throw new RuntimeException(String.format("Shipment with id - %d - doesn`t exist", id));
        shipmentRepository.delete(id);
        return true;
    }

    @Transactional
    public Shipment updateShipment(Shipment shipment) {
        var shipmentFromDb = shipmentRepository.findById(shipment.getId());
        var updatedShipment = new Shipment();
        if (shipmentFromDb.isPresent()) {
            updatedShipment = shipmentFromDb.get();
            updatedShipment.setShipmentDate(shipment.getShipmentDate());
            updatedShipment.setProduct(shipment.getProduct());
            updatedShipment.setCustomer(shipment.getCustomer());
            updatedShipment.setChain(shipment.getChain());
            updatedShipment.setVolume(shipment.getVolume());
            updatedShipment.setSalesValue(shipment.getSalesValue());
            updatedShipment.setPromoSign(shipment.getPromoSign());
            shipmentRepository.update(updatedShipment);
        }
        return updatedShipment;
    }

    @Override
    public List<Shipment> mapDataToObj(Map<Integer, List<String>> data) {
        var shipments = new ArrayList<Shipment>();
        var keys = data.keySet();
        for (int i = 1; i < keys.size() - 1; i++) {
            List<String> row = data.get(i);
            shipments.add(
                    Shipment.builder()
                            .shipmentDate(new Date(row.get(0)))
                            .product(productService.getProduct(Long.valueOf(row.get(1))))
                            .customer(customerService.getCustomer(Long.valueOf(row.get(2))))
                            .chain(chainService.getChain(Long.valueOf(row.get(3))))
                            .volume(Integer.valueOf(row.get(4)))
                            .salesValue(new BigDecimal(row.get(5)))
                            .promoSign(REGULAR)
                            .build()
            );
        }
        return shipments;
    }

    @Override
    public void saveDataFromExcel(MultipartFile file, String sheetName) throws IOException, InvalidFormatException {
        var data = Utils.readFromExcel(file, sheetName);
        var shipments = mapDataToObj(data);
        shipments.forEach(this::saveShipment);
    }

    /**
     * Выгрузка фактов продаж с учетом признака ПРОМО
     * @return список, включающий поля - сеть, категория, месяц, факт продаж в штуках по базавой цене, факт продаж по промо цене, доля продаж по промо(%)
     */
    public Collection<AnalysisResponse> getAnalysisWithPromoSign(){
        return shipmentRepository.getAnalysisWithPromoSign();
    }

    /**
     * Выгрузка фактов по дням с фильтрацией по сетям и продуктам
     * @param chains список id сетей
     * @param products список id продуктов
     * @return список, включающий поля - дата отгрузки, сеть, категория, продукт, покупатель, сумма продажи, общий объём в шт, признак ПРОМО
     **/
    public Collection<AnalysisResponse> getAnalysisWithDaysChainsAndProductsFilter(List<Integer> chains, List<Integer> products) {
        return shipmentRepository.getAnalysisWithChainsAndProductsFilter(chains, products);
    }

    /**
     * Проверка наличия признака ПРОМО цены
     *
     * @param shipment объект отгрузки
     * @return результат сравнения цен методом compareTo()
     */
    private int isPromoSign(Shipment shipment) {
        var price = shipment.getSalesValue();
        var vol = shipment.getVolume();
        var regularPriceValue = priceRepository.findById(shipment.getId()).orElseThrow().getRegularPrice();
        var pricePerUnit = price.divide(BigDecimal.valueOf(vol), RoundingMode.HALF_UP);
        return regularPriceValue.compareTo(pricePerUnit);
    }
}
