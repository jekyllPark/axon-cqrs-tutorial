package com.cqrs.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HolderDto {
    private String holderName;
    private String tel;
    private String address;
    /**
     * 새로온 요구사항 추가
     * */
    private String company;
}