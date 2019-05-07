package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Company;
import com.evaluation.domain.Turn;

public interface TurnService {

	public void register(Turn turn);

	public Optional<Turn> get(long tno);

	public void modify(Turn turn);

	public void remove(long tno);

	public List<Turn> getList(Company company);
}
