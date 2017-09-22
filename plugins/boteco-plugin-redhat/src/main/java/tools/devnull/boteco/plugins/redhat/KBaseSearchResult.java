/*
 * The MIT License
 *
 * Copyright (c) 2016 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Permission  is hereby granted, free of charge, to any person obtaining
 * a  copy  of  this  software  and  associated  documentation files (the
 * "Software"),  to  deal  in the Software without restriction, including
 * without  limitation  the  rights to use, copy, modify, merge, publish,
 * distribute,  sublicense,  and/or  sell  copies of the Software, and to
 * permit  persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The  above  copyright  notice  and  this  permission  notice  shall be
 * included  in  all  copies  or  substantial  portions  of the Software.
 *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT  WARRANTY OF ANY KIND,
 * EXPRESS  OR  IMPLIED,  INCLUDING  BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN  NO  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM,  DAMAGES  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT  OR  OTHERWISE,  ARISING  FROM,  OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE   OR   THE   USE   OR   OTHER   DEALINGS  IN  THE  SOFTWARE.
 */

package tools.devnull.boteco.plugins.redhat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KBaseSearchResult {

  private Response response;

  public boolean isEmpty() {
    return response.count == 0;
  }

  public List<Doc> results() {
    return response.docs;
  }

  public Doc result() {
    return response.docs.get(0);
  }

  public Response getResponse() {
    return response;
  }

  public void setResponse(Response response) {
    this.response = response;
  }

  public static class Response {

    @SerializedName("numFound")
    private int count;

    private List<Doc> docs;

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public List<Doc> getDocs() {
      return docs;
    }

    public void setDocs(List<Doc> docs) {
      this.docs = docs;
    }
  }

  public static class Doc {

    private String documentKind;
    private String uri;
    @SerializedName("view_uri")
    private String viewUri;
    private String title;
    private String id;
    private int caseCount;
    @SerializedName("solution.id")
    private String solutionId;

    public String getDocumentKind() {
      return documentKind;
    }

    public void setDocumentKind(String documentKind) {
      this.documentKind = documentKind;
    }

    public String getUri() {
      return uri;
    }

    public void setUri(String uri) {
      this.uri = uri;
    }

    public String getViewUri() {
      return viewUri;
    }

    public void setViewUri(String viewUri) {
      this.viewUri = viewUri;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getSolutionId() {
      return solutionId;
    }

    public void setSolutionId(String solutionId) {
      this.solutionId = solutionId;
    }

    public int getCaseCount() {
      return caseCount;
    }

    public void setCaseCount(int caseCount) {
      this.caseCount = caseCount;
    }
  }

}
