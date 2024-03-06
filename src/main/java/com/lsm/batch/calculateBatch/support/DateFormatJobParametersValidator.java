package com.lsm.batch.calculateBatch.support;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class DateFormatJobParametersValidator implements JobParametersValidator {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	private final String[] names;

	public DateFormatJobParametersValidator(String[] names) {
		this.names = names;
	}

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		for (String name : names) {
			validateDateFormat(parameters, name);
		}
	}

	private void validateDateFormat(JobParameters parameters, String name) throws JobParametersInvalidException {
		try {
			String str = parameters.getString(name);
			LocalDate.parse(Objects.requireNonNull(str), dateTimeFormatter);
		} catch (Exception e) {
			throw new JobParametersInvalidException("yyyyMMdd 형식만을 지원합니다.");
		}
	}
}
