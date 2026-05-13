package com.xzzj.medmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.dto.MedicineQueryRequest;
import com.xzzj.medmanager.dto.MedicineRequest;
import com.xzzj.medmanager.vo.MedicineVO;

public interface IMedicineService {

    IPage<MedicineVO> listMedicines(MedicineQueryRequest queryRequest);

    MedicineVO getMedicineById(Long id);

    MedicineVO createMedicine(MedicineRequest request);

    MedicineVO updateMedicine(Long id, MedicineRequest request);

    void deleteMedicine(Long id);
}