package com.xzzj.medmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.dto.RecordQueryRequest;
import com.xzzj.medmanager.vo.RecordVO;

public interface IRecordService {

    IPage<RecordVO> listRecords(RecordQueryRequest queryRequest);
}