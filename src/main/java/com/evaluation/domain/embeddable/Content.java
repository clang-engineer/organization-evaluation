package com.evaluation.domain.embeddable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <code>Content</code> 객체는 Book 객체에 속하는 회답 내용과 해당 회답을 점수를 표현한다
 */
@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Content {

    private String name;
    private double ratio;

}