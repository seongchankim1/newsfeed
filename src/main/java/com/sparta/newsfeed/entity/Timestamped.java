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
	private LocalDateTime write_date;    // 생성일자

	@LastModifiedDate
	@Column
	private LocalDateTime update_date;    // 수정일자

	private LocalDateTime status_changed;    // 상태변경시간

	public void updateStatusChanged() {
		this.status_changed = LocalDateTime.now();
	}

	public void updateProfileChanged() {
		this.update_date = LocalDateTime.now();
	}

	public void updateUpdateDate() {
		this.update_date = LocalDateTime.now();
	}

//	private LocalDateTime createDateTime;
//
//	@LastModifiedDate
//	@Column(name = "last_modified_date_time")
//	private LocalDateTime lastModifiedDateTime;
}





