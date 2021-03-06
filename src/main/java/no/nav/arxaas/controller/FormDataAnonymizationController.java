package no.nav.arxaas.controller;

import no.nav.arxaas.exception.UnableToReadInputStreamException;
import no.nav.arxaas.model.*;
import no.nav.arxaas.model.anonymity.AnonymizationResultPayload;
import no.nav.arxaas.service.AnonymizationService;
import no.nav.arxaas.service.LoggerService;
import no.nav.arxaas.utils.FormDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/anonymize/file")
public class FormDataAnonymizationController {

    private final AnonymizationService anonymizationService;
    private final LoggerService loggerService;
    private final FormDataFactory formDataFactory;

    @Autowired
    FormDataAnonymizationController(AnonymizationService anonymizationService, LoggerService loggerService, FormDataFactory formDataFactory) {
        this.anonymizationService = anonymizationService;
        this.loggerService = loggerService;
        this.formDataFactory = formDataFactory;
    }

    @PostMapping
    public AnonymizationResultPayload anonymization(@RequestPart("file") MultipartFile file, @RequestPart("metadata") @Valid FormMetaDataRequest payload, @RequestPart("hierarchies") MultipartFile[] hierarchies, HttpServletRequest request) {
        long requestRecivedTime = System.currentTimeMillis();
        Request requestPayload;
        try {
            requestPayload = formDataFactory.createAnonymizationPayload(file, payload, hierarchies);
        } catch (IOException e) {
            throw new UnableToReadInputStreamException(e.getMessage());
        }
        loggerService.loggPayload(requestPayload, request.getRemoteAddr(), FormDataAnonymizationController.class);
        AnonymizationResultPayload anonymizationResult = anonymizationService.anonymize(requestPayload);
        long requestProcessingTime = System.currentTimeMillis() - requestRecivedTime;
        loggerService.loggAnonymizeResult(anonymizationResult,requestProcessingTime, FormDataAnonymizationController.class, request.getRemoteAddr());
        return anonymizationResult;
    }
}
