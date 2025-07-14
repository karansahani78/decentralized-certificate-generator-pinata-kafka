package com.karan.online.certificate.generator.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequest {
    private String name;
    private String course;
    private String issuedBy;
    private String date; // You can use LocalDate if preferred
}
