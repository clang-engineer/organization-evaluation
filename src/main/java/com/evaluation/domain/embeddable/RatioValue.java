package com.evaluation.domain.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * Ratio와 평가 점수를 함께 전닫하기 위한 embeddable RelationMBO answers필드에서 사용.
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