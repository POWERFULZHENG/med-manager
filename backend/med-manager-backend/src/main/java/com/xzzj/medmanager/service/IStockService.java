package com.xzzj.medmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.dto.*;
import com.xzzj.medmanager.vo.StockVO;

import java.util.List;

public interface IStockService {

    IPage<StockVO> listStocks(StockQueryRequest queryRequest);

    StockVO getStockById(Long id);

    StockVO stockIn(StockInRequest request);

    void stockOut(StockOutRequest request);

    void adjustStock(Long id, StockAdjustRequest request);

    List<StockVO> getExpiringStocks();

    List<StockVO> getExpiredStocks();
}