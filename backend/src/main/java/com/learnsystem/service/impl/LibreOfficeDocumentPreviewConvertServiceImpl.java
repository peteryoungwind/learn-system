package com.learnsystem.service.impl;

import com.learnsystem.exception.BusinessException;
import com.learnsystem.service.DocumentPreviewConvertService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LibreOfficeDocumentPreviewConvertServiceImpl implements DocumentPreviewConvertService {
    private final String libreOfficeCommand;

    public LibreOfficeDocumentPreviewConvertServiceImpl(@Value("${app.preview.libreoffice-command:soffice}") String libreOfficeCommand) {
        this.libreOfficeCommand = libreOfficeCommand;
    }

    @Override
    public File convertToPdf(File sourceFile) {
        try {
            File outputDir = Files.createTempDirectory("learn-system-preview-").toFile();
            Process process = new ProcessBuilder(
                    libreOfficeCommand,
                    "--headless",
                    "--convert-to",
                    "pdf",
                    sourceFile.getAbsolutePath(),
                    "--outdir",
                    outputDir.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new BusinessException("文档转码失败");
            }
            String targetName = sourceFile.getName().replaceFirst("\\.[^.]+$", "") + ".pdf";
            File pdfFile = new File(outputDir, targetName);
            if (!pdfFile.exists()) {
                throw new BusinessException("文档转码结果不存在");
            }
            return pdfFile;
        } catch (IOException e) {
            throw new BusinessException("文档转码失败: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("文档转码被中断");
        }
    }
}
