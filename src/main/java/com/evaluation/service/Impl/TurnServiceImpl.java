package com.evaluation.service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.evaluation.domain.InfoSurvey;
import com.evaluation.domain.Turn;
import com.evaluation.persistence.TurnRepository;
import com.evaluation.service.TurnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TurnServiceImpl implements TurnService {

	@Setter(onMethod_ = { @Autowired })
	private TurnRepository turnRepo;

	@Transactional
	@Override
	public void register(Turn turn) {
		log.info("service : turn register " + turn);

		if (turn.getTypes().contains("360")) {
			InfoSurvey info360 = new InfoSurvey();
			info360.setTitle("-");
			turn.setInfo360(info360);
		}

		if (turn.getTypes().contains("MBO")) {
			InfoSurvey infoMbo = new InfoSurvey();
			infoMbo.setTitle("-");
			turn.setInfoMbo(infoMbo);
		}

		turnRepo.save(turn);
	}

	@Override
	public Optional<Turn> get(long tno) {
		log.info("service : turn get " + tno);
		return turnRepo.findById(tno);
	}

	@Override
	public void modify(Turn turn) {
		log.info("service : turn modify " + turn);

		turnRepo.findById(turn.getTno()).ifPresent(origin -> {
			origin.setTitle(turn.getTitle());
			origin.setTypes(turn.getTypes());

			// 기본 정보에 기본값 세팅하기 위한 분기 if문
			if (!turn.getTypes().contains("360")) {
				origin.setInfo360(null);
			}

			if (!turn.getTypes().contains("MBO")) {
				origin.setInfoMbo(null);
			}

			if (turn.getTypes().contains("360") && (turn.getInfo360() == null)) {
				InfoSurvey info360 = new InfoSurvey();
				info360.setTitle("-");
				origin.setInfo360(info360);
			}

			if (turn.getTypes().contains("MBO") && (turn.getInfoMbo() == null)) {
				InfoSurvey infoMbo = new InfoSurvey();
				infoMbo.setTitle("-");
				origin.setInfoMbo(infoMbo);
			}

			turnRepo.save(origin);
		});
	}

	@Override
	public void remove(long tno) {
		log.info("service : turn remove " + tno);
		turnRepo.deleteById(tno);
	}

	@Override
	public Optional<List<Turn>> getList(Long cno) {
		log.info("service : turn getList by " + cno);
		Optional<List<Turn>> result = turnRepo.getTurnsOfCompany(cno);
		return result;
	}

	@Override
	public Optional<List<Turn>> getListInSurvey(Long cno) {
		log.info("service : turn getList by " + cno);

		// 시간 제약 조건을 위해 localtime을 같이
		Optional<List<Turn>> result = turnRepo.getTurnsInSurvey(cno, LocalDateTime.now());
		return result;
	}

	@Override
	public void commentRegister(Turn turn) {
		turnRepo.save(turn);
	}
}
