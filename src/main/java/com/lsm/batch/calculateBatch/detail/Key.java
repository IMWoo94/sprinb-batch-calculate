package com.lsm.batch.calculateBatch.detail;

import java.io.Serializable;

record Key(Long customerId, Long serviceId) implements Serializable {
}
