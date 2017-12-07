package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PullRequestInfo implements Serializable, IsSerializable {
  private static final long serialVersionUID = 3354703304031517914L;

  private String idx;
  private String title;
  private String author;

  private HashMap<ProductType, ProductInfo> products;

  public static PullRequestInfo create() {
    return new PullRequestInfo();
  }

  public String idx() {
    return idx;
  }

  public PullRequestInfo idx(final String idx) {
    this.idx = idx;
    return this;
  }

  public String title() {
    return title;
  }

  public PullRequestInfo title(final String title) {
    this.title = title;
    return this;
  }

  public String author() {
    return author;
  }

  public PullRequestInfo author(final String author) {
    this.author = author;
    return this;
  }

  public HashMap<ProductType, ProductInfo> products() {
    return products;
  }

  public PullRequestInfo products(final HashMap<ProductType, ProductInfo> products) {
    this.products = products;
    return this;
  }
}
