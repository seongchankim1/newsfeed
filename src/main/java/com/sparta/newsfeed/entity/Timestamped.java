package com.sparta.newsfeed.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime writeDate;    // 생성일자

	@LastModifiedDate
	@Column
	private LocalDateTime updateDate;    // 수정일자

	public void updateProfileChanged() {
		this.updateDate = LocalDateTime.now();
	}

	public void updateUpdateDate() {
		this.updateDate = LocalDateTime.now();
	}

//	private LocalDateTime createDateTime;
//
//	@LastModifiedDate
//	@Column(name = "last_modified_date_time")
//	private LocalDateTime lastModifiedDateTime;
}





