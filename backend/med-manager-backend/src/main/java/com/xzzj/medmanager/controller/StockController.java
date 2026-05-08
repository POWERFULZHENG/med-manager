package com.xzzj.medmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzzj.medmanager.common.R;
import com.xzzj.medmanager.dto.*;
import com.xzzj.medmanager.service.IStockService;
import com.xzzj.medmanager.vo.StockVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private IStockService stockService;

    @GetMapping
    public R<IPage<StockVO>> listStocks(StockQueryRequest queryRequest) {
        IPage<StockVO> page = stockService.listStocks(queryRequest);
        return R.ok(page);
    }

    @GetMapping("/{id}")
    public R<StockVO> getStockById(@PathVariable Long id) {
        StockVO stock = stockService.getStockById(id);
        return R.ok(stock);
    }

    @PostMapping("/in")
    public R<StockVO> stockIn(@Valid @RequestBody StockInRequest request) {
        StockVO stock = stockService.stockIn(request);
        return R.ok(stock);
    }

    @PostMapping("/out")
    public R<Void> stockOut(@Valid @RequestBody StockOutRequest request) {
        stockService.stockOut(request);
        return R.ok();
    }

    @PutMapping("/{id}/adjust")
    public R<Void> adjustStock(@PathVariable Long id, @Valid @RequestBody StockAdjustRequest request) {
        stockService.adjustStock(id, request);
        return R.ok();
    }

    @GetMapping("/expiring")
    public R<List<StockVO>> getExpiringStocks() {
        List<StockVO> stocks = stockService.getExpiringStocks();
        return R.ok(stocks);
    }

    @GetMapping("/expired")
    public R<List<StockVO>> getExpiredStocks() {
        List<StockVO> stocks = stockService.getExpiredStocks();
        return R.ok(stocks);
    }
}