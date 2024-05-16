package com.xatkit.example;

import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.dsl.state.* ;

// this class acts like a global variable stuff.
// serves as a global ref for both of the type bots
public final class StateLocal {
  public static ReactPlatform reactPlatform;
  public static BodyStep awaitingInput;
}
