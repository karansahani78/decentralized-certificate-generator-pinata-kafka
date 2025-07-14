package com.karan.online.certificate.generator.controller;

import com.karan.online.certificate.generator.model.CertificateMetadata;
import com.karan.online.certificate.generator.model.CertificateRequest;
import com.karan.online.certificate.generator.repository.CertificateMetadataRepo;
import com.karan.online.certificate.generator.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateMetadataRepo certificateMetadataRepo;

    public CertificateController(CertificateService certificateService,
                                 CertificateMetadataRepo certificateMetadataRepo) {
        this.certificateService = certificateService;
        this.certificateMetadataRepo = certificateMetadataRepo;
    }

    /**
     *  POST /api/certificates/issue
     * Issues a new certificate and returns its metadata
     */
    @PostMapping("/issue")
    public ResponseEntity<CertificateMetadata> issueCertificate(@RequestBody CertificateRequest request) {
        try {
            CertificateMetadata metadata = certificateService.issueCertificate(request);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/certificates/verify/{id}
     * Verifies the certificate by its MongoDB ID (UUID)
     */
    @GetMapping("/verify/{id}")
    public ResponseEntity<CertificateMetadata> verifyCertificate(@PathVariable Long id) {
        Optional<CertificateMetadata> metadata = certificateMetadataRepo.findById(id);
        return metadata.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
