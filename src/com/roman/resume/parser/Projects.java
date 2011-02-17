// Copyright (c) 2010, Roman Khmelichek
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
//  1. Redistributions of source code must retain the above copyright notice,
//     this list of conditions and the following disclaimer.
//  2. Redistributions in binary form must reproduce the above copyright notice,
//     this list of conditions and the following disclaimer in the documentation
//     and/or other materials provided with the distribution.
//  3. Neither the name of Roman Khmelichek nor the names of its contributors
//     may be used to endorse or promote products derived from this software
//     without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
// EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
// OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
// OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
// ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.roman.resume.parser;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.roman.resume.parser.SnippetState.Snippet;

public class Projects extends ResumeElement {
  private SnippetState snippetState = new SnippetState();
  public ArrayList<ArrayList<Snippet>> projects = new ArrayList<ArrayList<Snippet>>();
  private boolean projectP = false;
  private int projectC = 0;
  private int lastProjectC = 0;

  public Projects(DefaultHandler parent, XMLReader parser) {
    super(parent, parser);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    snippetState.startSpecialSnippet(qName);

    if (qName.equals("project") && include(attributes)) {
      System.out.println("START project");
      projectP = true;
      projectC++;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("projects")) {
      System.out.println("END projects");
      parser.setContentHandler(parent);
    }

    snippetState.endSpecialSnippet(qName);

    if (qName.equals("project") && projectP) {
      if (projects.size() > 0) {
        System.out.println(projects.get(projects.size() - 1));
      }
      System.out.println("END project");
      projectP = false;
      projectC++;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (projectP) {
      String str = new String(ch, start, length);
      snippetState.setCurrSnippet(str);

      if (projectC != lastProjectC) {
        ArrayList<Snippet> snippets = new ArrayList<Snippet>();
        snippets.add(snippetState.getSnippet(str));
        projects.add(snippets);
        lastProjectC = projectC;
      } else {
        ArrayList<Snippet> snippets = projects.get(projects.size() - 1);
        snippets.add(snippetState.getSnippet(str));
      }
    }
  }
}
