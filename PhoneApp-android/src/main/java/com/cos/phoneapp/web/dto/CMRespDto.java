package com.cos.phoneapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CMRespDto<T> {
	private int code;
	private T data;
}
// 항상 이 dto로 응답한다. -> JSON으로 응답 한다.
