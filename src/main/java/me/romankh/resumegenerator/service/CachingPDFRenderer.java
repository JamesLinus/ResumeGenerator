package me.romankh.resumegenerator.service;

import java.io.OutputStream;

/**
 * @author Roman Khmelichek
 */
public interface CachingPDFRenderer {
  void render(OutputStream pdfOs) throws Exception;
}
