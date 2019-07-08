package com.evaluation.domain.embeddable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * <code>Leader</code> 객체는 Department 객체에 속하는 리더 정보를 표현한다
 */
@Embeddable
@Getter
@Setter
public class Leader {

    private long sno;

    private String title;
    private String content;
}
