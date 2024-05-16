package com.xatkit.example.helpers;

import java.util.*;
import lombok.*;
import com.xatkit.example.helpers.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString// <--- THIS is it
public class Account {
  private Long id;
  private String name;
  private String spec;
  private String key;
  private List<Config> configs;
}
