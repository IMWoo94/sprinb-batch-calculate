package com.lsm.batch.dormantbatch;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JobExecution {

	private BatchStatus status;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

}
