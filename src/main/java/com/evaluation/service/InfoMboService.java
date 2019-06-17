package com.evaluation.service;

import com.evaluation.domain.InfoMbo;

public interface InfoMboService {
	public void register(Long tno, InfoMbo infoMbo);

	public InfoMbo read(long tno);

	public void modify(Long tno, InfoMbo infoMbo);

	public void remove(long tno);
}
