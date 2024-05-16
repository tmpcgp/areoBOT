package com.xatkit.example;

public final class Page {
  public String id; // uniquely identifies the paging object;
  public int cursor_begin = 0;
  public int cursor_end   = 0;

  public Page(){}
  public Page(
    String id
  ){
    this.id = id;
  }

}
