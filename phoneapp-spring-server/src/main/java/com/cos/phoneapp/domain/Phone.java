package com.cos.phoneapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // persistence
public class Phone {
	@Id //pk
	@GeneratedValue(strategy = GenerationType.IDENTITY) // autoIncrease
	private long id;
	
	private String name;
	private String tel;
}
