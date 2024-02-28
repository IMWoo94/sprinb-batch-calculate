package com.lsm.batch.dormantbatch;

public interface ItemWriter<O> {

	void write(O item);
}
