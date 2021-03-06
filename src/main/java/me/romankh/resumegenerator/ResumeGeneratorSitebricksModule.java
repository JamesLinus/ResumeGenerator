package me.romankh.resumegenerator;

import com.google.sitebricks.SitebricksModule;
import com.google.sitebricks.SitebricksServletModule;
import com.google.sitebricks.client.transport.Json;
import me.romankh.resumegenerator.web.filter.Utf8OutputFilter;
import me.romankh.resumegenerator.web.pages.ResumeHtmlPage;
import me.romankh.resumegenerator.web.transport.JaxbJsonTransport;

/**
 * @author Roman Khmelichek
 */
public class ResumeGeneratorSitebricksModule extends SitebricksModule {
  public ResumeGeneratorSitebricksModule() {
    super();
  }

  @Override
  protected SitebricksServletModule servletModule() {
    return new SitebricksServletModule() {
      @Override
      protected void configurePreFilters() {
        filter("/*").through(Utf8OutputFilter.class);
      }

      @Override
      protected void configurePostFilters() {
      }

      @Override
      protected void configureCustomServlets() {
      }
    };
  }

  @Override
  protected void configureSitebricks() {
    scan(ResumeHtmlPage.class.getPackage());

    bind(Json.class).to(JaxbJsonTransport.class);
  }
}
