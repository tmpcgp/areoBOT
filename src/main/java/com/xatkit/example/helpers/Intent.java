package com.xatkit.example.helpers;

import lombok.*;
import java.util.*;
import com.xatkit.example.helpers.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString// <--- THIS is it
public class Intent {
  private Long id;
  private String name;
  private List<String> trainingSentences;
  private Config config;
  private State state;
}
