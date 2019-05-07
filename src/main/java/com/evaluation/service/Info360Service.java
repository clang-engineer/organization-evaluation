package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.Info360;

public interface Info360Service {
	public void register(Info360 info360);

	public Optional<Info360> get(long tno);

	public void modify(Info360 info360);

	public void remove(long tno);
}
