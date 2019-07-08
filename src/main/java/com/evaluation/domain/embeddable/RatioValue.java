package com.evaluation.domain.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * <code>RatioValue</code> 객체는 Ratio와 평가 점수를 함께 전닫하기 위해 RelationMBO answers필드에서
 * 사용된다.
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