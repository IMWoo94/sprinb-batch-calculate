package com.lsm.batch.dormantbatch;

public interface ItemProcessor<I, O> {

	O process(I item);
}
