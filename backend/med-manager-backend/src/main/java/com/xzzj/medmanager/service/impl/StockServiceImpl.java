package com.xzzj.medmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzzj.medmanager.dto.*;
import com.xzzj.medmanager.entity.InventoryRecord;
import com.xzzj.medmanager.entity.Medicine;
import com.xzzj.medmanager.entity.MedicineStock;
import com.xzzj.medmanager.mapper.InventoryRecordMapper;
import com.xzzj.medmanager.mapper.MedicineMapper;
import com.xzzj.medmanager.mapper.MedicineStockMapper;
import com.xzzj.medmanager.service.IStockService;
import com.xzzj.medmanager.vo.StockVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements IStockService {

    @Autowired
    private MedicineStockMapper stockMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private InventoryRecordMapper recordMapper;

    @Override
    public IPage<StockVO> listStocks(StockQueryRequest queryRequest) {
        Page<MedicineStock> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        LambdaQueryWrapper<MedicineStock> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(queryRequest.getMedicineName())) {
            List<Long> medicineIds = getMedicineIdsByName(queryRequest.getMedicineName());
            if (medicineIds.isEmpty()) {
                return new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
            }
            wrapper.in(MedicineStock::getMedicineId, medicineIds);
        }

        if (StringUtils.hasText(queryRequest.getStorageLocation())) {
            wrapper.eq(MedicineStock::getStorageLocation, queryRequest.getStorageLocation());
        }

        if (queryRequest.getIsExpiring() != null && queryRequest.getIsExpiring()) {
            LocalDate expiringDate = LocalDate.now().plusDays(30);
            wrapper.ge(MedicineStock::getExpireDate, LocalDate.now())
                   .le(MedicineStock::getExpireDate, expiringDate);
        }

        if (queryRequest.getIsExpired() != null && queryRequest.getIsExpired()) {
            wrapper.lt(MedicineStock::getExpireDate, LocalDate.now());
        }

        wrapper.gt(MedicineStock::getQuantity, 0)
               .orderByAsc(MedicineStock::getExpireDate);

        IPage<MedicineStock> stockPage = stockMapper.selectPage(page, wrapper);

        return stockPage.convert(this::convertToVO);
    }

    @Override
    public StockVO getStockById(Long id) {
        MedicineStock stock = stockMapper.selectById(id);
        if (stock == null) {
            throw new RuntimeException("Stock not found");
        }
        return convertToVO(stock);
    }

    @Override
    @Transactional
    public StockVO stockIn(StockInRequest request) {
        Medicine medicine = medicineMapper.selectById(request.getMedicineId());
        if (medicine == null) {
            throw new RuntimeException("Medicine not found");
        }

        MedicineStock stock = new MedicineStock();
        BeanUtils.copyProperties(request, stock);
        stock.setCreatedAt(LocalDateTime.now());
        stock.setUpdatedAt(LocalDateTime.now());
        stockMapper.insert(stock);

        InventoryRecord record = new InventoryRecord();
        record.setStockId(stock.getId());
        record.setMedicineId(request.getMedicineId());
        record.setOperationType("IN");
        record.setQuantity(request.getQuantity());
        record.setOperator("system");
        record.setRemark(request.getRemark());
        record.setCreatedAt(LocalDateTime.now());
        recordMapper.insert(record);

        return convertToVO(stock);
    }

    @Override
    @Transactional
    public void stockOut(StockOutRequest request) {
        MedicineStock stock = stockMapper.selectById(request.getStockId());
        if (stock == null) {
            throw new RuntimeException("Stock not found");
        }

        if (stock.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        stock.setQuantity(stock.getQuantity() - request.getQuantity());
        stock.setUpdatedAt(LocalDateTime.now());
        stockMapper.updateById(stock);

        InventoryRecord record = new InventoryRecord();
        record.setStockId(request.getStockId());
        record.setMedicineId(stock.getMedicineId());
        record.setOperationType("OUT");
        record.setQuantity(request.getQuantity());
        record.setOperator("system");
        record.setRemark(request.getRemark());
        record.setCreatedAt(LocalDateTime.now());
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void adjustStock(Long id, StockAdjustRequest request) {
        MedicineStock stock = stockMapper.selectById(id);
        if (stock == null) {
            throw new RuntimeException("Stock not found");
        }

        Integer oldQuantity = stock.getQuantity();
        stock.setQuantity(request.getQuantity());
        stock.setUpdatedAt(LocalDateTime.now());
        stockMapper.updateById(stock);

        InventoryRecord record = new InventoryRecord();
        record.setStockId(id);
        record.setMedicineId(stock.getMedicineId());
        record.setOperationType("ADJUST");
        record.setQuantity(request.getQuantity() - oldQuantity);
        record.setOperator("system");
        record.setRemark(request.getRemark());
        record.setCreatedAt(LocalDateTime.now());
        recordMapper.insert(record);
    }

    @Override
    public List<StockVO> getExpiringStocks() {
        LocalDate expiringDate = LocalDate.now().plusDays(30);

        LambdaQueryWrapper<MedicineStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(MedicineStock::getExpireDate, LocalDate.now())
               .le(MedicineStock::getExpireDate, expiringDate)
               .gt(MedicineStock::getQuantity, 0)
               .orderByAsc(MedicineStock::getExpireDate);

        List<MedicineStock> stocks = stockMapper.selectList(wrapper);
        return stocks.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<StockVO> getExpiredStocks() {
        LambdaQueryWrapper<MedicineStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(MedicineStock::getExpireDate, LocalDate.now())
               .gt(MedicineStock::getQuantity, 0)
               .orderByAsc(MedicineStock::getExpireDate);

        List<MedicineStock> stocks = stockMapper.selectList(wrapper);
        return stocks.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private List<Long> getMedicineIdsByName(String medicineName) {
        LambdaQueryWrapper<Medicine> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Medicine::getMedicineName, medicineName);
        List<Medicine> medicines = medicineMapper.selectList(wrapper);
        return medicines.stream().map(Medicine::getId).collect(Collectors.toList());
    }

    private StockVO convertToVO(MedicineStock stock) {
        StockVO vo = new StockVO();
        BeanUtils.copyProperties(stock, vo);

        Medicine medicine = medicineMapper.selectById(stock.getMedicineId());
        if (medicine != null) {
            vo.setMedicineName(medicine.getMedicineName());
            vo.setSpecification(medicine.getSpecification());
            vo.setManufacturer(medicine.getManufacturer());
            vo.setUnit(medicine.getUnit());
        }

        if (stock.getExpireDate() != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), stock.getExpireDate());
            vo.setExpireDays((int) days);
        }

        return vo;
    }
}