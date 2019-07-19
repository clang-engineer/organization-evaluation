package com.evaluation.service;

import java.util.Optional;

import com.evaluation.domain.HelpDesk;
import com.evaluation.vo.PageVO;

import org.springframework.data.domain.Page;

/**
 * <code>HelpDeskService</code> 객체는 HelpDesk sevice 계층의 interface를 표현한다.
 */
public interface HelpDeskService {

    /**
     * 문의를 등록한다.
     * 
     * @param helpDesk 문의 객체
     */
    void register(HelpDesk helpDesk);

    /**
     * 문의를 읽어온다.
     * 
     * @param hno 문의 id
     * @return 문의 객체
     */
    Optional<HelpDesk> read(long hno);

    /**
     * 문의를 수정한다.
     * 
     * @param HelpDesk 문의 객체
     */
    void modify(HelpDesk helpDesk);

    /**
     * 문의를 삭제한다.
     * 
     * @param hno 문의 id
     */
    void remove(long hno);

    /**
     * 문의 리스트를 출력한다.
     * 
     * @param page 페이지 정보
     * @return 문의 리스트
     */
    Page<HelpDesk> getList(PageVO page);
}