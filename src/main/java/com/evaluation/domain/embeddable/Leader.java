package com.evaluation.domain.embeddable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * Department에서 사용
 */
@Embeddable
@Getter
@Setter
public class Leader {

    private long sno;

    private String title;
    private String content;
}
