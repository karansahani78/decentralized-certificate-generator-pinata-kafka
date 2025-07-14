package com.karan.online.certificate.generator.service;

import com.karan.online.certificate.generator.config.CertificateProducer;
import com.karan.online.certificate.generator.event.CertificateEvent;
import com.karan.online.certificate.generator.model.CertificateMetadata;
import com.karan.online.certificate.generator.model.CertificateRequest;
import com.karan.online.certificate.generator.repository.CertificateMetadataRepo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CertificateService {

    private final PDFGeneratorService pdfGenerator;
    private final QRCodeService qrCodeService;
    private final PinataService pinataService;
    private final CertificateMetadataRepo repository;
    private final KafkaTemplate<String, CertificateEvent> kafkaTemplate;

    public CertificateService(
            PDFGeneratorService pdfGenerator,
            QRCodeService qrCodeService,
            PinataService pinataService,
            CertificateMetadataRepo repository,
            KafkaTemplate<String, CertificateEvent> kafkaTemplate) {
        this.pdfGenerator = pdfGenerator;
        this.qrCodeService = qrCodeService;
        this.pinataService = pinataService;
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public CertificateMetadata issueCertificate(CertificateRequest request) throws Exception {
        String certificateId = UUID.randomUUID().toString();

        // Step 1: Generate certificate PDF
        byte[] pdfBytes = pdfGenerator.generateCertificatePDF(request,certificateId);

        // Step 2: Upload PDF to IPFS via Pinata
        String ipfsPdfUrl = pinataService.uploadToIPFS(pdfBytes, certificateId + ".pdf");

        // Step 3: Generate QR code pointing to the IPFS PDF link
        byte[] qrCodeBytes = qrCodeService.generateQRCode(ipfsPdfUrl);
        String ipfsQrUrl = pinataService.uploadToIPFS(qrCodeBytes, certificateId + "-qr.png");

        // Step 4: Save metadata in DB
        CertificateMetadata metadata = new CertificateMetadata();
        metadata.setName(request.getName());
        metadata.setCourse(request.getCourse());
        metadata.setIssuedBy(request.getIssuedBy());
        metadata.setIssueDate(LocalDate.parse(request.getDate()));
        metadata.setIpfsUrl(ipfsPdfUrl);
        metadata.setQrCodeUrl(ipfsQrUrl);
        metadata.setIssuedAt(LocalDateTime.now());

        CertificateMetadata savedMetadata = repository.save(metadata);

        // Step 5: Publish event to Kafka
        CertificateEvent event = new CertificateEvent(
                certificateId,
                request.getName(),
                request.getCourse(),
                ipfsPdfUrl,
                ipfsQrUrl
        );
        kafkaTemplate.send("certificate-events", event);

        return savedMetadata;
    }
}
