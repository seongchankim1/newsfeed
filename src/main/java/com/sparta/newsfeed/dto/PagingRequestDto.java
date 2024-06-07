package com.sparta.newsfeed.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRequestDto {

	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String sortBy;

	public PagingRequestDto (LocalDateTime startDate, LocalDateTime endDate, String sortBy) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.sortBy = sortBy;
	}
}
