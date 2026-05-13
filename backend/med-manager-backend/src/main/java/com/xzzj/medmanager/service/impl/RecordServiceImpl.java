package com.xzzj.medmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzzj.medmanager.dto.RecordQueryRequest;
import com.xzzj.medmanager.entity.InventoryRecord;
import com.xzzj.medmanager.entity.Medicine;
import com.xzzj.medmanager.mapper.InventoryRecordMapper;
import com.xzzj.medmanager.mapper.MedicineMapper;
import com.xzzj.medmanager.service.IRecordService;
import com.xzzj.medmanager.vo.RecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl implements IRecordService {

    @Autowired
    private InventoryRecordMapper recordMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Override
    public IPage<RecordVO> listRecords(RecordQueryRequest queryRequest) {
        Page<InventoryRecord> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        LambdaQueryWrapper<InventoryRecord> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(queryRequest.getMedicineName())) {
            List<Long> medicineIds = getMedicineIdsByName(queryRequest.getMedicineName());
            if (medicineIds.isEmpty()) {
                return new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
            }
            wrapper.in(InventoryRecord::getMedicineId, medicineIds);
        }

        if (StringUtils.hasText(queryRequest.getOperationType())) {
            wrapper.eq(InventoryRecord::getRecordType, queryRequest.getOperationType());
        }

        if (queryRequest.getStartTime() != null) {
            wrapper.ge(InventoryRecord::getCreatedAt, queryRequest.getStartTime());
        }

        if (queryRequest.getEndTime() != null) {
            wrapper.le(InventoryRecord::getCreatedAt, queryRequest.getEndTime());
        }

        wrapper.orderByDesc(InventoryRecord::getCreatedAt);

        IPage<InventoryRecord> recordPage = recordMapper.selectPage(page, wrapper);

        return recordPage.convert(this::convertToVO);
    }

    private List<Long> getMedicineIdsByName(String medicineName) {
        LambdaQueryWrapper<Medicine> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Medicine::getMedicineName, medicineName);
        List<Medicine> medicines = medicineMapper.selectList(wrapper);
        return medicines.stream().map(Medicine::getId).collect(Collectors.toList());
    }

    private RecordVO convertToVO(InventoryRecord record) {
        RecordVO vo = new RecordVO();
        BeanUtils.copyProperties(record, vo);

        Medicine medicine = medicineMapper.selectById(record.getMedicineId());
        if (medicine != null) {
            vo.setMedicineName(medicine.getMedicineName());
            vo.setSpecification(medicine.getSpecification());
            vo.setUnit(medicine.getUnit());
        }

        return vo;
    }
}