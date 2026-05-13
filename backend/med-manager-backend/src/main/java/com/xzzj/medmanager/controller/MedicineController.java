package com.xzzj.medmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.common.result.R;
import com.xzzj.medmanager.dto.MedicineQueryRequest;
import com.xzzj.medmanager.dto.MedicineRequest;
import com.xzzj.medmanager.service.IMedicineService;
import com.xzzj.medmanager.vo.MedicineVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private IMedicineService medicineService;

    @GetMapping
    public R<IPage<MedicineVO>> listMedicines(MedicineQueryRequest queryRequest) {
        IPage<MedicineVO> page = medicineService.listMedicines(queryRequest);
        return R.ok(page);
    }

    @GetMapping("/{id}")
    public R<MedicineVO> getMedicineById(@PathVariable Long id) {
        MedicineVO medicine = medicineService.getMedicineById(id);
        return R.ok(medicine);
    }

    @PostMapping
    public R<MedicineVO> createMedicine(@Valid @RequestBody MedicineRequest request) {
        MedicineVO medicine = medicineService.createMedicine(request);
        return R.ok(medicine);
    }

    @PutMapping("/{id}")
    public R<MedicineVO> updateMedicine(@PathVariable Long id, @Valid @RequestBody MedicineRequest request) {
        MedicineVO medicine = medicineService.updateMedicine(id, request);
        return R.ok(medicine);
    }

    @DeleteMapping("/{id}")
    public R<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return R.ok();
    }
}