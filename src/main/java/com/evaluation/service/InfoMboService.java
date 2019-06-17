package com.evaluation.service;

import com.evaluation.domain.InfoMBO;

public interface InfoMBOService {
	public void register(Long tno, InfoMBO infoMBO);

	public InfoMBO read(long tno);

	public void modify(Long tno, InfoMBO infoMBO);

	public void remove(long tno);
}
