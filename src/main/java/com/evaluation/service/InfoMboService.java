package com.evaluation.service;

import com.evaluation.domain.embeddable.InfoMbo;

public interface InfoMboService {
	void register(Long tno, InfoMbo infoMbo);

	InfoMbo read(long tno);

	void modify(Long tno, InfoMbo infoMbo);

	void remove(long tno);
}
