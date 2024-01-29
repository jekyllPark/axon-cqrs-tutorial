package com.cqrs.query.version;

import com.cqrs.event.HolderCreationEvent;
import org.axonframework.serialization.SimpleSerializedType;
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation;
import org.axonframework.serialization.upcasting.event.SingleEventUpcaster;

public class HolderCreationEventV1 extends SingleEventUpcaster {
    /*
    * 최초에는 revision이 없었기에 null 인자 넘김.
    * */
    private static SimpleSerializedType targetType = new SimpleSerializedType(HolderCreationEvent.class.getTypeName(), null);
    @Override
    protected boolean canUpcast(IntermediateEventRepresentation intermediateRepresentation) {
        return intermediateRepresentation.getType().equals(targetType);
    }

    /*
    * event 버전 확인 후, 이전 버전 들어왔을 때의 처리 로직
    * dom4j는 xml 파일을 역, 직렬화 해주는 라이브러리
    * */
    @Override
    protected IntermediateEventRepresentation doUpcast(IntermediateEventRepresentation intermediateRepresentation) {
        return intermediateRepresentation.upcastPayload(
                new SimpleSerializedType(targetType.getName(), "1.0"),
                org.dom4j.Document.class,
                doc -> {
                    doc.getRootElement().addElement("company").setText("N/A");
                    return doc;
                }
        );
    }
}
