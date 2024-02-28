package com.lsm.batch.application;

public interface ItemWriter<O> {

	void write(O item);
}
