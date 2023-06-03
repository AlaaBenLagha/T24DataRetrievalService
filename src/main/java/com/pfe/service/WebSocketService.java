//package com.pfe.service;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import com.pfe.model.T24Cheque;
//
//@Service
//public class WebSocketService {
//
//	@Autowired
//    private SimpMessagingTemplate template;
//
//    public void emitCheque(T24Cheque cheque) {
//        template.convertAndSend("/topic/cheques", cheque);
//    }
//
//    
// 
//  
//    
//}
