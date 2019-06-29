package com.evaluation.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * Ratio와 평가 점수를 함께 전닫하기 위한 embeddable
 */
@Embeddable
@Getter
@Setter
public class RatioValue {

    @Column(name = "answer_ratio")
    private Double ratio;
    @Column(name = "answer_value")
    private Double value;

}