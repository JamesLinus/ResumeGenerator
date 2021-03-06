package me.romankh.resumegenerator.service.impl;

import me.romankh.resumegenerator.annotations.binding.Defaults;
import me.romankh.resumegenerator.annotations.binding.XSLT;
import me.romankh.resumegenerator.configuration.Prop;
import me.romankh.resumegenerator.configuration.Property;
import me.romankh.resumegenerator.service.InputFileResolver;
import me.romankh.resumegenerator.service.ResumeGeneratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author Roman Khmelichek
 */
@Singleton
public class CachingXSLTPDFRenderer extends AbstractCachingPDFRenderer {
  private static final Logger logger = LogManager.getLogger(CachingXSLTPDFRenderer.class);

  private final String resumeXmlPath;
  private final String resumeXslPath;
  private final InputFileResolver inputFileResolver;
  private final ResumeGeneratorService resumeGeneratorService;

  private volatile byte[] cachedPdfBytes;
  private volatile Date resumeXmlLastModifiedDate;
  private volatile Date resumeXslLastModifiedDate;

  @Inject
  public CachingXSLTPDFRenderer(@Prop(Property.RESUME_XML_PATH) String resumeXmlPath,
                                @Prop(Property.RESUME_XSL_PATH) String resumeXslPath,
                                @Defaults boolean useDefaults,
                                InputFileResolver inputFileResolver,
                                @XSLT ResumeGeneratorService resumeGeneratorService) {
    this.resumeXmlPath = resumeXmlPath;
    this.resumeXslPath = resumeXslPath;
    this.inputFileResolver = inputFileResolver;
    this.resumeGeneratorService = resumeGeneratorService;
  }

  public synchronized void render(OutputStream pdfOs) throws Exception {
    Date xmlModifiedSinceDate = inputFileResolver.getDateModifiedSince(resumeXmlPath, resumeXmlLastModifiedDate);
    Date xslModifiedSinceDate = inputFileResolver.getDateModifiedSince(resumeXslPath, resumeXslLastModifiedDate);
    if (cachedPdfBytes == null || xmlModifiedSinceDate != null || xslModifiedSinceDate != null) {
      ByteArrayOutputStream cachingOutputStream = new ByteArrayOutputStream();
      resumeGeneratorService.render(cachingOutputStream);
      cachingOutputStream.flush();
      cachedPdfBytes = cachingOutputStream.toByteArray();
      resumeXmlLastModifiedDate = xmlModifiedSinceDate;
      resumeXslLastModifiedDate = xslModifiedSinceDate;
    }

    writeCachedBytes(cachedPdfBytes, pdfOs);
  }
}
