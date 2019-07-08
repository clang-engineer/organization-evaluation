package com.evaluation.service;

import java.util.List;
import java.util.Optional;

import com.evaluation.domain.Turn;

public interface TurnService {

	void register(Turn turn);

	Optional<Turn> read(long tno);

	void modify(Turn turn);

	void remove(long tno);

	Optional<List<Turn>> getTurnsOfCompany(Long cno);

	Optional<List<Turn>> getTurnsInSurvey(Long cno);

	Optional<List<Turn>> getTurnsInMbo(Long cno);

	void commentRegister(Turn turn);
}
