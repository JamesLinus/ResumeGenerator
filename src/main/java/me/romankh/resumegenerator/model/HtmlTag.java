package me.romankh.resumegenerator.model;

import org.eclipse.persistence.oxm.annotations.XmlValueExtension;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;

/**
 * @author Roman Khmelichek
 */
@XmlRootElement(name = "html")
public class HtmlTag extends Tag {
  @XmlValueExtension
  @XmlValue
  @XmlMixed
  @XmlAnyElement
  private List<Object> value;

  public List<Object> getValue() {
    return value;
  }

  public HtmlTag setValue(List<Object> value) {
    this.value = value;
    return this;
  }
}
