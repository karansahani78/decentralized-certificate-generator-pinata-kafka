package com.karan.online.certificate.generator.service;

import com.karan.online.certificate.generator.model.CertificateRequest;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFGeneratorService {

    public byte[] generateCertificatePDF(CertificateRequest request, String certId) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 14);

        document.add(new Paragraph("Certificate of Completion", titleFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("This certifies that", normalFont));
        document.add(new Paragraph(request.getName(), new Font(Font.HELVETICA, 18, Font.BOLD)));
        document.add(new Paragraph("has successfully completed the course:", normalFont));
        document.add(new Paragraph(request.getCourse(), new Font(Font.HELVETICA, 16, Font.BOLDITALIC)));
        document.add(new Paragraph("Issued By: " + request.getIssuedBy(), normalFont));
        document.add(new Paragraph("Issue Date: " + request.getDate(), normalFont));
        document.add(new Paragraph("Certificate ID: " + certId, normalFont));

        document.close();
        return out.toByteArray();
    }
}
