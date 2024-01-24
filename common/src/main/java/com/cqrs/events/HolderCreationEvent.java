package com.cqrs.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.serialization.Revision;

@AllArgsConstructor
@ToString
@Getter
@Revision("1.0") // 요구사항 변경으로 인한 이벤트 변경 마킹, 이로 인해 이벤트 버전을 저장하여 event upcasting시 사용됨.
public class HolderCreationEvent {
    private String holderId;
    private String holderName;
    private String tel;
    private String address;
    private String company;
}
