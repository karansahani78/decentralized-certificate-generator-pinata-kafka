package com.karan.online.certificate.generator.repository;

import com.karan.online.certificate.generator.model.CertificateMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateMetadataRepo extends JpaRepository<CertificateMetadata, Long> {
}
