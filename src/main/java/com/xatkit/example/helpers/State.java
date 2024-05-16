package com.xatkit.example.helpers;

import java.util.*;
import com.xatkit.example.helpers.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString// <--- THIS is it
public class State {
  private Long id;
  private String name;
  private List<String> answers;
  private Config config;
  private List<Choice> choices;
  private Intent onIntent;
}
