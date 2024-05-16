package com.xatkit.example.helpers;

import lombok.*;
import java.util.*;
import com.xatkit.example.helpers.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString// <--- THIS is it
public class Config {
  private Long id;
  private String name;
  private Account account;
  private List<State>  states;
  private List<Intent> intents;
}
