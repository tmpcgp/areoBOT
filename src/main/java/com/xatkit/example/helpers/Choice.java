package com.xatkit.example.helpers;

import lombok.*;
import com.google.gson.*;
import com.xatkit.example.helpers.Tuteur;
import com.google.common.hash.*;
import com.google.gson.JsonObject;
import com.xatkit.core.XatkitBot;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.socket.action.*;
import com.xatkit.core.recognition.*;
import com.xatkit.library.core.CoreLibrary;
import com.xatkit.example.helpers.*;
import com.xatkit.core.recognition.nlpjs.*;
import com.xatkit.dsl.state.* ;
import com.xatkit.dsl.intent.*;
import com.xatkit.dsl.model.UseEventStep;
import com.xatkit.core.server.HttpMethod;
import com.xatkit.core.server.RestHandlerFactory;
import com.xatkit.core.XatkitException;
import com.xatkit.core.platform.io.WebhookEventProvider;
import com.xatkit.execution.StateContext;
import com.xatkit.util.FileUtils;
import com.xatkit.plugins.react.platform.action.*;
import com.xatkit.plugins.react.platform.io.*;

import fr.inria.atlanmod.commons.log.Log;

import org.apache.commons.configuration2.Configuration;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.net.HttpURLConnection.*;
import java.net.URL;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;

import static java.util.Objects.isNull;
import static fr.inria.atlanmod.commons.Preconditions.checkArgument;
import static com.xatkit.example.helpers.Utils.*;
import static com.xatkit.example.helpers.Intents.*;
import static com.xatkit.dsl.DSL.eventIs;
import static com.xatkit.dsl.DSL.fallbackState;
import static com.xatkit.dsl.DSL.intent;
import static com.xatkit.dsl.DSL.intentIs;
import static com.xatkit.dsl.DSL.model;
import static com.xatkit.dsl.DSL.state;




@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString// <--- THIS is it
public class Choice {
  private Long id;
  private String name;
  private State state;
  private State redirectValue;

  public static BodyStep scanBS (BodyStep awaitingInput, ReactPlatform reactplatform, Choice c, BodyStep[] bss, List<State> states, IntentMandatoryTrainingSentenceStep[] imss ) {
    for ( int i = 0; i < states.size(); i ++ ) {
      // check if the redirect_value matches the state's name
      if (states.get(i).getName().equals(c.getRedirectValue())) {
        // there's a match initialize the stuff in the bodystep ArrayList
        // if the it hasn't been initialized before.
        if (bss[i] != null) {
          return bss[i];
        }
        else {
          return Utils.makeBodyStep(awaitingInput, reactplatform, states.get(i), states, bss, imss);
        }
      }
      else continue;
    }
    return null;
  }

  public static List<String> toArrStr(List<Choice> choices) {
    ArrayList<String> ret = new ArrayList<>();
    for (Choice c : choices) { ret.add(c.getName()); }
    return ret;
  }
}
