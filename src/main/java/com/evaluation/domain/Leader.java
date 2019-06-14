package com.evaluation.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Leader {

    private long sno;

    private String title;
    private String content;
}
