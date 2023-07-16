package com.pfe.controller;

import com.pfe.model.T24Cheque;
import com.pfe.service.T24ChequeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ImageUrlController {

    private final T24ChequeService t24ChequeService;

    @Autowired
    public ImageUrlController(T24ChequeService t24ChequeService) {
        this.t24ChequeService = t24ChequeService;
    }

    @PostMapping("/api/image-urls/{id}")
    public void receiveImageUrls(@PathVariable String id, @RequestBody List<String> urls) {
        T24Cheque cheque = t24ChequeService.getOne(id);
        cheque.setRefSignature(urls);
        if (urls.size() > 0) {
            cheque.setSignaturePath1(urls.get(0));  // set the first path
        }
        if (urls.size() > 1) {
            cheque.setSignaturePath2(urls.get(1));  // set the second path
        }
        t24ChequeService.save(cheque);
    }

}
