package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Turn;

public interface TurnService {

	public void register(Turn turn);

	public Optional<Turn> read(long tno);

	public void modify(Turn turn);

	public void remove(long tno);

	public Optional<List<Turn>> getTurnsOfCompany(Long cno);

	public Optional<List<Turn>> getTurnsInSurvey(Long cno);

	public Optional<List<Turn>> getTurnsInMbo(Long cno);

	public void commentRegister(Turn turn);
}
