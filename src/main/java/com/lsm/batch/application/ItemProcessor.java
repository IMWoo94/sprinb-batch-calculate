package com.lsm.batch.application;

public interface ItemProcessor<I, O> {

	O process(I item);
}
