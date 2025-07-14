package com.karan.online.certificate.generator.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateEvent {
    private String certificateId;
    private String name;
    private String course;
    private String ipfsUrl;
    private String qrCodeUrl;
}
