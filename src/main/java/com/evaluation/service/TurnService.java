package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Turn;

public interface TurnService {

	public void register(Turn turn);

	public Optional<Turn> get(long tno);

	public void modify(Turn turn);

	public void remove(long tno);

	public Optional<List<Turn>> getList(Long cno);

	public Optional<List<Turn>> getListInSurvey(Long cno);

	public void commentRegister(Turn turn);
}
