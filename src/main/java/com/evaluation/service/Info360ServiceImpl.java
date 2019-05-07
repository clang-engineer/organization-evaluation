package com.evaluation.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evaluation.domain.Info360;
import com.evaluation.persistence.Info360Repository;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Info360ServiceImpl implements Info360Service {

	@Setter(onMethod_ = { @Autowired })
	private Info360Repository info360Repo;

	@Override
	public void register(Info360 info360) {
		log.info("service : info360 register " + info360);
		info360Repo.save(info360);
	}

	@Override
	public Optional<Info360> get(long tno) {
		log.info("service : info360 get " + tno);
		return info360Repo.findById(tno);
	}

	@Override
	public void modify(Info360 info360) {
		log.info("service : info360 modify " + info360);
		info360Repo.save(info360);
	}

	@Override
	public void remove(long tno) {
		log.info("service : info360 remove " + tno);
		info360Repo.deleteById(tno);
	}

}
