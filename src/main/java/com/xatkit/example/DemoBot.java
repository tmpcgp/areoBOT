package com.xatkit.example;

import fr.inria.atlanmod.commons.log.Log;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;

import static com.xatkit.example.helpers.Utils.*;
import static com.xatkit.example.helpers.Intents.*;
import static com.xatkit.dsl.DSL.eventIs;
import static com.xatkit.dsl.DSL.fallbackState;
import static com.xatkit.dsl.DSL.intent;
import static com.xatkit.dsl.DSL.intentIs;
import static com.xatkit.dsl.DSL.model;
import static com.xatkit.dsl.DSL.state;


import com.xatkit.example.StateLocal;
import com.xatkit.core.XatkitBot;
import com.xatkit.core.server.XatkitServer;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.execution.ExecutionModel;
import com.xatkit.plugins.react.platform.socket.action.*;
import com.xatkit.core.recognition.*;
import com.xatkit.library.core.CoreLibrary;
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
import com.google.gson.*;
import com.xatkit.plugins.react.platform.io.*;
import com.xatkit.dsl.state.impl.*;
import com.xatkit.example.helpers.*;
import com.xatkit.dsl.state.impl.StateBuilder;
import com.xatkit.dsl.state.StateProvider;
import com.corundumstudio.socketio.SocketIOServer;
import com.xatkit.dsl.model.ExecutionModelProvider;
import com.xatkit.plugins.react.platform.action.*;

import org.apache.commons.configuration2.Configuration;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.http.impl.bootstrap.ServerBootstrap;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.SocketTimeoutException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;

import lombok.val;
import lombok.NonNull;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static com.xatkit.dsl.DSL.eventIs;
import static com.xatkit.dsl.DSL.fallbackState;
import static com.xatkit.dsl.DSL.intent;
import static com.xatkit.dsl.DSL.model;
import static com.xatkit.dsl.DSL.intentIs;
import static com.xatkit.dsl.DSL.state;
import static com.xatkit.example.helpers.Intents.*;
import static com.xatkit.example.helpers.Utils.*;


public class DemoBot {

    // might be collisions with configs.
    // writing and not writing ... ( is it good for our http server ? )
    private static ExecutionModel botModel;
    private static Configuration botConfig;
    private static XatkitBot bot;

    private static int PORT_Xatkit = 5_000; // there're should be a setter into the stuff.
    private static int PORT        = 6666;

    private static void shut() {
      bot.shutdown(); 
      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
      System.out.println("@shutting down the xatkit bot @shut()");
      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
    }

    public static void re() {
      String[] p = {};
      shut();

      StateLocal.awaitingInput = null;
      StateLocal.reactPlatform = null;

      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
      System.out.println("@restarting the xatkit bot @main()");
      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");

      main(p);
    }

   // PRINT EVAL LOOP
  public static void main(String[] args) {
    System.out.println("@main parsing config and initializing values.");

    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
    System.out.println("@main @config value " + ContainerLocal.current );
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");


    // but before lets initialize the glabals
    StateLocal.reactPlatform = new ReactPlatform();
    StateLocal.awaitingInput = state("AwaitingInput");

    ReactEventProvider reactEventProvider   = StateLocal.reactPlatform.getReactEventProvider();
    ReactIntentProvider reactIntentProvider = StateLocal.reactPlatform.getReactIntentProvider();

    final long curr = System.nanoTime();

    // inti config states and intents in order to avoid the null ptr
    
    if (ContainerLocal.current.getStates() == null) {
      ContainerLocal.current.setStates(new ArrayList<State>());
    } if (ContainerLocal.current.getIntents() == null) {
      ContainerLocal.current.setIntents(new ArrayList<Intent>());
    }

    final IntentMandatoryTrainingSentenceStep[] imtss = new IntentMandatoryTrainingSentenceStep[ContainerLocal.current.getIntents().size()];
    final BodyStep[] bss                              = makeBodyStepArr( StateLocal.awaitingInput, StateLocal.reactPlatform, ContainerLocal.current.getStates(), imtss );
    StateLocal.awaitingInput                          = constructGM( imtss );

    // finished the parsing
    final long end   = System.nanoTime();
    final long delta = end - curr;

    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
    System.out.println("@Parsing took " + delta + " ms.");
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");

    final BodyStep handleWelcome  = state("HandleWelcome");
    final BodyStep init           = state("Init");

    init
      .next()
      .when(eventIs(ReactEventProvider.ClientReady)).moveTo(handleWelcome);

    handleWelcome
      .body(context -> {

        // should we give the user the ability to change that ?
        StateLocal.reactPlatform.reply(context, "Salut ! Je m'appelle 🔥roseBot🔥");
        StateLocal.reactPlatform.reply(context, "Je suis enchanté/e de faire ta rencontre🤗");
        StateLocal.reactPlatform.reply(context, "Comment vas-tu aujourd'hui?");

      }).next().moveTo(StateLocal.awaitingInput);

    StateProvider defaultFallback = fallbackState()
      .body(context -> {
        StateLocal.reactPlatform.reply(context, "Excuse-moi, mais je n'ai pas bien saisi🤭");
    });

    ExecutionModelProvider botModel = model()
      .usePlatform(StateLocal.reactPlatform)
      .listenTo(reactEventProvider)
      .listenTo(reactIntentProvider)
      .initState(init)
      .defaultFallbackState(defaultFallback);

    botConfig = new BaseConfiguration();

    botConfig.addProperty(IntentRecognitionProviderFactory.INTENT_PROVIDER_KEY, NlpjsConfiguration.NLPJS_INTENT_PROVIDER);
    botConfig.addProperty(NlpjsConfiguration.AGENT_ID_KEY, "default");
    botConfig.addProperty(NlpjsConfiguration.LANGUAGE_CODE_KEY, "en");
    botConfig.addProperty(NlpjsConfiguration.NLPJS_SERVER_KEY, "http://localhost:" + PORT ); 

    bot = new XatkitBot(botModel, botConfig);
    bot.run();

    // this is only on the client side ( i.e it's not the production stuff )
    bot.getXatkitServer().registerRestEndpoint(HttpMethod.POST, "/configd/create",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {

      final JsonObject config_obj = content.getAsJsonObject();

      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
      System.out.println("@config_obj " + config_obj);
      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");

      final Config config_json    = new Gson().fromJson( config_obj, Config.class );
      ContainerLocal.current      = config_json;

      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
      System.out.println("@config_json " + config_json);
      System.out.println("@config-demo path");
      System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");

      JsonObject statusObject = new JsonObject();
      statusObject.addProperty("msg", "Saved the current Config");

      return statusObject;
    }));

    bot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/configd/create",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      System.out.println("@config-demo path ( options )");
      return statusObject;
    }));
  }
}
