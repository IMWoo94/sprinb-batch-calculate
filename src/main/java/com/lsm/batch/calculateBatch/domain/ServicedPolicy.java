package com.lsm.batch.calculateBatch.domain;

import lombok.Getter;

@Getter
public enum ServicedPolicy {
	A(1L, "/services/a", 10),
	B(2L, "/services/b", 10),
	C(3L, "/services/c", 10),
	D(4L, "/services/d", 15),
	E(5L, "/services/e", 15),
	F(6L, "/services/f", 10),
	G(7L, "/services/g", 10),
	H(8L, "/services/h", 10),
	I(9L, "/services/i", 10),
	J(10L, "/services/j", 10),
	K(11L, "/services/k", 10),
	L(12L, "/services/l", 12),
	M(13L, "/services/m", 12),
	N(14L, "/services/n", 12),
	O(15L, "/services/o", 10),
	P(16L, "/services/p", 10),
	Q(17L, "/services/q", 10),
	R(18L, "/services/r", 10),
	S(19L, "/services/s", 10),
	T(20L, "/services/t", 10),
	U(21L, "/services/u", 10),
	V(22L, "/services/v", 10),
	W(23L, "/services/w", 19),
	X(24L, "/services/x", 19),
	Y(25L, "/services/y", 19),
	Z(26L, "/services/z", 19);

	private final Long id;
	private final String url;
	private final Integer fee;

	ServicedPolicy(Long id, String url, Integer fee) {
		this.id = id;
		this.url = url;
		this.fee = fee;
	}
}
