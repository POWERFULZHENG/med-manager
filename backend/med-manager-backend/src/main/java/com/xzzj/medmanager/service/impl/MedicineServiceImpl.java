package com.xzzj.medmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzzj.medmanager.dto.MedicineQueryRequest;
import com.xzzj.medmanager.dto.MedicineRequest;
import com.xzzj.medmanager.entity.Medicine;
import com.xzzj.medmanager.mapper.MedicineMapper;
import com.xzzj.medmanager.service.IMedicineService;
import com.xzzj.medmanager.vo.MedicineVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class MedicineServiceImpl implements IMedicineService {

    @Autowired
    private MedicineMapper medicineMapper;

    @Override
    public IPage<MedicineVO> listMedicines(MedicineQueryRequest queryRequest) {
        Page<Medicine> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        
        LambdaQueryWrapper<Medicine> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryRequest.getMedicineName()), 
                     Medicine::getMedicineName, queryRequest.getMedicineName())
               .like(StringUtils.hasText(queryRequest.getManufacturer()), 
                     Medicine::getManufacturer, queryRequest.getManufacturer())
               .eq(StringUtils.hasText(queryRequest.getDosageForm()), 
                   Medicine::getDosageForm, queryRequest.getDosageForm())
               .orderByDesc(Medicine::getCreatedAt);
        
        IPage<Medicine> medicinePage = medicineMapper.selectPage(page, wrapper);
        
        return medicinePage.convert(this::convertToVO);
    }

    @Override
    public MedicineVO getMedicineById(Long id) {
        Medicine medicine = medicineMapper.selectById(id);
        if (medicine == null) {
            throw new RuntimeException("Medicine not found");
        }
        return convertToVO(medicine);
    }

    @Override
    public MedicineVO createMedicine(MedicineRequest request) {
        Medicine medicine = new Medicine();
        BeanUtils.copyProperties(request, medicine);
        medicine.setCreatedAt(LocalDateTime.now());
        medicine.setUpdatedAt(LocalDateTime.now());
        medicineMapper.insert(medicine);
        return convertToVO(medicine);
    }

    @Override
    public MedicineVO updateMedicine(Long id, MedicineRequest request) {
        Medicine medicine = medicineMapper.selectById(id);
        if (medicine == null) {
            throw new RuntimeException("Medicine not found");
        }
        BeanUtils.copyProperties(request, medicine);
        medicine.setUpdatedAt(LocalDateTime.now());
        medicineMapper.updateById(medicine);
        return convertToVO(medicine);
    }

    @Override
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineMapper.selectById(id);
        if (medicine == null) {
            throw new RuntimeException("Medicine not found");
        }
        medicineMapper.deleteById(id);
    }

    private MedicineVO convertToVO(Medicine medicine) {
        MedicineVO vo = new MedicineVO();
        BeanUtils.copyProperties(medicine, vo);
        return vo;
    }
}