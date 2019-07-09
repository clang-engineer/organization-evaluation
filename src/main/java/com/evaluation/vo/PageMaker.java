package com.evaluation.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import groovy.transform.ToString;
import lombok.Getter;

/**
 * <code>PageMaker</code> 객체는 pageing에 필요한 객체를 구성하고 표현한다.
 */
@Getter
@ToString
public class PageMaker<T> {

	private Page<T> result;

	private Pageable prevPage;
	private Pageable nextPage;

	private int currentPageNum;
	private int totalPageNum;

	private Pageable currentPage;

	private List<Pageable> pageList;

	public PageMaker(Page<T> result) {

		this.result = result;
		this.currentPage = result.getPageable();
		this.currentPageNum = currentPage.getPageNumber() + 1;
		this.totalPageNum = result.getTotalPages();
		this.pageList = new ArrayList<>();

		calcPages();
	}

	private void calcPages() {
		int tempEndNum = (int) (Math.ceil(this.currentPageNum / 10.0) * 10);
		int startNum = tempEndNum - 9;

		Pageable startPage = this.currentPage;

		for (int i = startNum; i < this.currentPageNum; i++) {
			startPage = startPage.previousOrFirst();
		}
		this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

		if (this.totalPageNum < tempEndNum) {
			tempEndNum = this.totalPageNum;
		}

		for (int i = startNum; i <= tempEndNum; i++) {
			pageList.add(startPage);
			startPage = startPage.next();
		}

		// 마지막 this.nextPage에 할당되는 값은 화면의 마지막 페이지 번호가 전체 페이지 수보다 작으면 next에 startPage를
		// 할당하고, 아니면 null을 할당.
		this.nextPage = startPage.getPageNumber() < this.totalPageNum ? startPage : null;
	}
}
