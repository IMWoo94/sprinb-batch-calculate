package com.lsm.batch.dormantbatch;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum BatchStatus {

	STARTING,
	FAILED,
	COMPLETED;
}
