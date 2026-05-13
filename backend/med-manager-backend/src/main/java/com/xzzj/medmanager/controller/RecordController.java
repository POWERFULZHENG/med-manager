package com.xzzj.medmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.common.result.R;
import com.xzzj.medmanager.dto.RecordQueryRequest;
import com.xzzj.medmanager.service.IRecordService;
import com.xzzj.medmanager.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private IRecordService recordService;

    @GetMapping
    public R<IPage<RecordVO>> listRecords(RecordQueryRequest queryRequest) {
        IPage<RecordVO> page = recordService.listRecords(queryRequest);
        return R.ok(page);
    }
}