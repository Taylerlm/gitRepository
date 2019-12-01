package com.leyou.lmhitysu.order.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {
    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    /**
     * 当前机器id
     */
    private long workerId;
    /**
     * 序列号
     */
    private long dataCenterId;
}
