package com.cos.phoneapp.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.phoneapp.domain.Phone;
import com.cos.phoneapp.service.PhoneService;
import com.cos.phoneapp.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PhoneController {
	
	private final PhoneService phoneService;
	
	@GetMapping("/phone")
	public CMRespDto<?> findAll() {
		
		return new CMRespDto<>(1, phoneService.전체보기());
		// 안드로이드에서 요청했을 때 1이 안나온다? -> 무조건 오류
	}
	
	@GetMapping("/phone/{id}")
	public CMRespDto<?> findById(@PathVariable Long id) {
		
		return new CMRespDto<>(1, phoneService.상세보기(id));
	}
	
	@DeleteMapping("/phone/{id}")
	public CMRespDto<?> deleteById(@PathVariable Long id){
	
		phoneService.삭제하기(id);
		return null;
	}
	
	@PutMapping("/phone/{id}")
	public CMRespDto<?> updateById(@PathVariable Long id, @RequestBody Phone phone){
	
		return new CMRespDto<>(1, phoneService.수정하기(id, phone));
	}
	
	@PostMapping("/phone")
	public CMRespDto<?> save(@RequestBody Phone phone){
	
		return new CMRespDto<>(1, phoneService.저장하기(phone));
	}
}
