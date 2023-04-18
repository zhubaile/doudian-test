package com.jinritemai.cloud.base.core.controller;

import com.alibaba.fastjson.JSON;
import com.jinritemai.cloud.base.core.dto.SpiRequestDTO;
import com.jinritemai.cloud.base.core.dto.SpiResponseDTO;
import com.jinritemai.cloud.base.core.service.SpiService;
import com.jinritemai.cloud.base.core.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * author       hahafeng
 * email        chenzhifeng.777@bytedance.com
 * date         2022/7/16 11:16 AM
 */
@RestController
@RequestMapping("/base")
@Slf4j
public class CloudBaseController {
    @Autowired
    private SpiService spiService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String queryRedis(String prefix) {
        log.info("hello~");
        return String.valueOf(System.currentTimeMillis());
    }

    @PostMapping("/cloud/spi")
    public SpiResponseDTO handleSpi(@RequestBody SpiRequestDTO spiRequestDTO, @RequestHeader(value = LogUtils.LOG_ID_KEY, required = false) String logId) {
        LogUtils.setLogId(logId);
        try {
            return spiService.handle(spiRequestDTO);
        } finally {
            LogUtils.removeLogId();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        String successMsg = "{\"status\":\"UP\"}";
        String failedMsg = "{\"status\":\"DOWN\"}";
        log.info("发起健康探测");
        return new ResponseEntity<>(successMsg, HttpStatus.OK);
//        try {
//            ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8080/actuator/health", String.class);
//
//            if (responseEntity != null && responseEntity.getStatusCodeValue() == 200 && successMsg.equals(responseEntity.getBody())) {
//                log.info("健康探测结果正常");
//                return new ResponseEntity<>(successMsg, HttpStatus.OK);
//            }
//            log.error("Health Check is fail! resp: {}", JSON.toJSONString(responseEntity));
//        } catch (Exception e) {
//            log.error("Failed to call /actuator/health!", e);
//        }
//
//        return new ResponseEntity<>(failedMsg, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
