package com.xatkit.example;

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
import com.xatkit.dsl.model.ExecutionModelProvider;
import com.xatkit.plugins.react.platform.action.*;
import com.xatkit.plugins.react.platform.io.*;
import com.google.gson.*;
import com.xatkit.core.server.HttpMethod;
import com.xatkit.core.server.RestHandlerFactory;

import java.util.*;
import java.net.HttpURLConnection.*;
import java.net.URL;

import lombok.val;

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

import io.github.cdimascio.dotenv.Dotenv;

public class ProdBot {

  // port of the nlp-js server.
  private static XatkitBot xatkitBot;

  private static int PORT           = 6666;
  private static final String token = Dotenv.configure().directory(".").filename("env").load().get("token").trim(); 

  static {
  }

  private static void shut() {
    xatkitBot.shutdown(); 
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
    System.out.println("@shutting down the xatkit bot @shut()");
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
  }

  public static void re   () {
    String[] p = {};
    shut();
    main(p);
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
    System.out.println("@restarting the xatkit bot @main()");
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
  }

  public static void main(String[] args) {
    System.out.println("@main parsing config and initializing values.");

    StateProd.reactPlatform                 = new ReactPlatform();
    ReactEventProvider reactEventProvider   = StateProd.reactPlatform.getReactEventProvider();
    ReactIntentProvider reactIntentProvider = StateProd.reactPlatform.getReactIntentProvider();
    StateProd.awaitingInput                 = state("AwaitingInput");


    // taking all the configs.
    // of all the users.
    // so first get all teh accounts
    // and then get all the configs
    /*
    final ArrayList<Account> accs   = vomit();
    */

    ArrayList<Intent> intents = new ArrayList<>();
    ArrayList<State> states   = new ArrayList<>();

    final long curr_ = System.nanoTime();
    final long curr  = System.nanoTime();

    try {
      URL url            = new URL("http://localhost:8080/api/v1/account/?api_key="+token);
      JsonArray obj      = make_request_get_at_with(url, "accounts").getAsJsonArray("accounts");
      Account[] accounts = new Gson().fromJson(obj.toString(), Account[].class);

      for (Account account:accounts) {
        long id = account.getId().longValue();

        url              = new URL(String.format("http://localhost:8080/api/v1/account/%d/config?api_key=%s", id, token));
        obj              = make_request_get_at_with(url, "configs").getAsJsonArray("configs");
        Config[] configs = new Gson().fromJson(obj.toString(), Config[].class);

        for (Config config:configs) {
          for (Intent intent:config.getIntents()) {
            intents.add(intent);
          }
          for (State state:config.getStates()) {
            states.add(state);
          }
        }
      }

    } catch (Exception e) {
      System.out.println("|||||||||||||||||||||||||||");
      System.out.println("@computing all accounts");
      System.out.println("|||||||||||||||||||||||||||");
      System.out.println("@e "+e);
    }
    
    final long end_ = System.nanoTime();

    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
    System.out.println("@Loading took " + ((end_ - curr_)/1_000_000) + " ms.");
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");

    final IntentMandatoryTrainingSentenceStep[] imtss = new IntentMandatoryTrainingSentenceStep[intents.size()];
    final BodyStep[] bss                              = makeBodyStepArr( StateProd.awaitingInput, StateProd.reactPlatform, states, imtss );
    StateProd.awaitingInput                           = constructGM( imtss );

    final long end = System.nanoTime();

    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");
    System.out.println("@Parsing took " + ((end - curr)/1_000_000) + " ms.");
    System.out.println("-------------------------------|||||||||||||||||||||||||||||||||--------------------------------");


    // defaults
    // feel free to improve those ***Intents_.java***
    final IntentMandatoryTrainingSentenceStep menu      = intent("Menu").trainingSentences(menu_);
    final IntentMandatoryTrainingSentenceStep suggest   = intent("Sugg").trainingSentences(sugg_);
    final IntentMandatoryTrainingSentenceStep cheat     = intent("Cheat").trainingSentences(cheat_);
    final IntentMandatoryTrainingSentenceStep thankful  = intent("Thankful").trainingSentences( thankful_ );
    final IntentMandatoryTrainingSentenceStep howAreYou = intent("HowAreYou").trainingSentences(howAreYou_);
    final IntentMandatoryTrainingSentenceStep sad       = intent ( "Sad" ).trainingSentences( sad_ );
    final IntentMandatoryTrainingSentenceStep origins   = intent( "Origin" ).trainingSentences( origins_ );
    final IntentMandatoryTrainingSentenceStep happy     = intent ( "Happy" ).trainingSentences( joy_ );
    final IntentMandatoryTrainingSentenceStep greetings = intent ("Greetings" ).trainingSentences( greetings_ );

    // this is the basic and default state for everything
    // cannot customized it ?
    final BodyStep init             = state("Init");
    final BodyStep handleSaved      = state ( "Saved" );
    final BodyStep handleWelcome    = state("HandleWelcome");
    final BodyStep handleWhatsUp    = state("HandleWhatsUp");
    final BodyStep handleThankful   = state("HandleThankful");
    final BodyStep handleCheat      = state("HandleCheat");
    final BodyStep handleHappy      = state("HandleHappy");
    final BodyStep handleSad        = state("HandleSad");
    final BodyStep handleGreetings  = state ( "handleGreetings" );
    final BodyStep handleOrigins    = state ( "handleOrigins" );
    final BodyStep handleSuggestion = state ( "handleSuggestion" );
    final BodyStep handleIndecis    = state ( "handleIndecis" );
    final BodyStep handleFaitInt    = state ( "handleFaitInt ");

    init
      .next()
      .when(eventIs(ReactEventProvider.ClientReady)).moveTo(handleWelcome);


    StateProd.awaitingInput
      .next()
      .when(intentIs(origins))  .moveTo( handleOrigins )
      .when(intentIs(thankful)) .moveTo(handleThankful)
      .when(intentIs(greetings)).moveTo( handleGreetings )
      .when(intentIs(howAreYou)).moveTo(handleWhatsUp)
      .when(intentIs(cheat))    .moveTo (handleCheat);

    handleWelcome
      .body(context -> {
        StateProd.reactPlatform.reply(context, "Salut ! Je m'appelle 🔥roseBot🔥");
        StateProd.reactPlatform.reply(context, "Je suis enchanté/e de faire ta rencontre🤗");
        StateProd.reactPlatform.reply(context, "Comment vas-tu aujourd'hui?");
      })
      .next()
        .when(intentIs(happy)).moveTo( handleHappy )
        .when(intentIs(cheat)).moveTo( handleCheat )
        .when(intentIs(howAreYou)).moveTo( handleWhatsUp )
        .when(intentIs( sad )).moveTo ( handleSad )
      .fallback ( context -> {
        StateProd.reactPlatform.reply ( context, "Eh, et alors ? 😵");
      });

    handleWhatsUp
      .body(context -> StateProd.reactPlatform.reply(context, "Je vais bien💯 Merci."))
      .next()
      .moveTo ( StateProd.awaitingInput );

    handleOrigins
      .body ( context -> {
        // do we really need some function for that ?
        for ( String ans : fromAns() ) {
          StateProd.reactPlatform.reply ( context, ans );
        }
      })
      .next ()
      .moveTo(StateProd.awaitingInput);

    handleIndecis
      .body ( context -> {
        StateProd.reactPlatform.reply( context, "Ouf! Je vois que tu n'es pas sure pour ton future❓🤷‍♂️");
        StateProd.reactPlatform.reply( context, "Je te conseille fortement de prendre un rdv avec ton **API**👩🏻‍💻");
      })
      .next()
      .moveTo ( StateProd.awaitingInput);

    handleHappy
      .body( context -> StateProd.reactPlatform.reply(context, "Wow, je suis très content pour toi 👌") )
      .next()
      .moveTo(StateProd.awaitingInput);

    handleGreetings
      .body (
        context -> StateProd.reactPlatform.reply ( context, "Salut👋")
      )
      .next()
      .moveTo ( StateProd.awaitingInput );

    handleSad
      .body( context -> {
        StateProd.reactPlatform.reply(context, "Les temps sont très difficiles😓");
        StateProd.reactPlatform.reply(context, "Voici une citation pour te motiver💪");
        StateProd.reactPlatform.reply(context, giveMeQuote());
      })
      .next()
      .moveTo(StateProd.awaitingInput);

    handleFaitInt
      .body( context -> {
        StateProd.reactPlatform.reply (context, "**``Savais-tu que``** " + giveMeFact() );
      })
      .next()
      .moveTo ( StateProd.awaitingInput );

    handleThankful
      .body( context -> StateProd.reactPlatform.reply(context, "Avec plaisir 😊"))
      .next()
      .moveTo(StateProd.awaitingInput);

    handleCheat
      .body(context -> StateProd.reactPlatform.reply(context, "Désolé je ne peux pas répondre à ce genre de question 🥺") )
      .next()
      .moveTo ( StateProd.awaitingInput );

    handleSuggestion
      .body ( context -> StateProd.reactPlatform.reply ( context, "Super ! Quelle est cette merveilleuse idée 🧐"))
      .next ()
        .when ( intentIs( CoreLibrary.AnyValue ).and ( context -> {
          String sugg = ( String ) context.getIntent().getValue("value");
          saveSugg ( sugg );

          return true;
        })).moveTo ( handleSaved );

    handleSaved
      .body ( context -> {
        StateProd.reactPlatform.reply ( context, "Merçi, j'ai mémorisé ta magnifique suggestion dans ma base de donnée!");
        StateProd.reactPlatform.reply ( context, "Je l'ai aussi envoyé à mon supérieur😉");
      })
      .next()
      .moveTo ( StateProd.awaitingInput );

    val defaultFallback = fallbackState()
      .body(context -> {
        StateProd.reactPlatform.reply(context, "Excuse-moi, mais je n'ai pas bien saisi🤭");
      });

    // this is also defaulted
    ExecutionModelProvider botModel = model()
            .usePlatform(StateProd.reactPlatform)
            .listenTo(reactEventProvider)
            .listenTo(reactIntentProvider)
            .initState(init)
            .defaultFallbackState(defaultFallback);

    Configuration botConfiguration = new BaseConfiguration();

    botConfiguration.addProperty(IntentRecognitionProviderFactory.INTENT_PROVIDER_KEY, NlpjsConfiguration.NLPJS_INTENT_PROVIDER);
    botConfiguration.addProperty(NlpjsConfiguration.AGENT_ID_KEY, "default");
    botConfiguration.addProperty(NlpjsConfiguration.LANGUAGE_CODE_KEY, "en");
    botConfiguration.addProperty(NlpjsConfiguration.NLPJS_SERVER_KEY, "http://localhost:" + PORT ); 

    xatkitBot = new XatkitBot(botModel, botConfiguration);
    xatkitBot.run();

    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.POST, "/config/create",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("||||||||||||||||||||||||||||");
      System.out.println("@/config/create");
      System.out.println("||||||||||||||||||||||||||||");
      try {
        final JsonObject config_obj    = content.getAsJsonObject();
        final String id                = params.get(0).getValue();
        final URL url                  = new URL(String.format("http://localhost:8080/api/v1/account/%s/config?api_key=%s",id,token));

        return make_request_post_at_with(url, config_obj);
      } catch (Exception e) {
        System.out.println(e);
      }

      return null;
    }));

    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.POST, "/auth",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("||||||||||||||||||||||||||||");
      System.out.println("@/auth");
      System.out.println("||||||||||||||||||||||||||||");
      try {
        final JsonObject account = content.getAsJsonObject();
        final URL url            = new URL(String.format("http://localhost:8080/api/v1/account/auth?api_key=%s",token));

        return make_request_post_at_with(url, account);
      } catch (Exception e) {
        System.out.println("@/auth");
        System.out.println(e);
      }   

      return null;
    }));

    // for testing
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.GET, "/ping",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("||||||||||||||||||||||||||");
      System.out.println("@/ping");
      System.out.println("||||||||||||||||||||||||||");
      JsonObject statusObject = new JsonObject();
      statusObject.addProperty("msg", "pong");
      return statusObject;
    }));

    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.GET, "/config/delete",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("||||||||||||||||||||||||||||");
      System.out.println("@/config/delete");
      System.out.println("||||||||||||||||||||||||||||");

      try {
        final String id          = params.get(0).getValue();
        final URL url            = new URL(String.format("http://localhost:8080/api/v1/account/config/%s?api_key=%s", id, token));

        return make_request_delete_at_with(url);
      } catch (Exception e) {
        System.out.println("@/config/delete");
        System.out.println(e);
      }

      return null;
    }));

    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.GET, "/config/all",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("||||||||||||||||||||||||||||");
      System.out.println("@/config/all");
      System.out.println("||||||||||||||||||||||||||||");

      try {
        final String id = params.get(0).getValue();
        final URL url   = new URL(String.format("http://localhost:8080/api/v1/account/%s/config?api_key=%s", id, token));

        return make_request_get_at_with(url, "configs");
      } catch (Exception e) {
        System.out.println("@/config/all");
        System.out.println(e);
      }

      return null;
    }));

    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.PUT, "/config/update",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("|||||||||||||||||||||||||||||||||||||");
      System.out.println("@/config/update");
      System.out.println("|||||||||||||||||||||||||||||||||||||");
      try {
        final JsonObject config = content.getAsJsonObject();
        final String id         = params.get(0).getValue();
        final URL url           = new URL(String.format("http://localhost:8080/api/v1/account/%s/config?api_key=%s", id, token));

        return make_request_put_at_with(url, config);
      } catch (Exception e) {
        System.out.println("@/config/update path");
        System.out.println(e);
      }

      return null;
    }));

    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.POST, "/account/create",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("|||||||||||||||||||||||||||||||||||||");
      System.out.println("@/account/create");
      System.out.println("|||||||||||||||||||||||||||||||||||||");
      try {
        final JsonObject account = content.getAsJsonObject();
        final URL url            = new URL(String.format("http://localhost:8080/api/v1/account/?api_key=%s",token));

        return make_request_post_at_with(url, account);
      } catch (Exception e) {
        System.out.println("@/account/create path");
        System.out.println(e);
      }

      return null;
    }));

    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.PUT, "/account/update",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      System.out.println("|||||||||||||||||||||||||||||||||||||");
      System.out.println("@/account/update");
      System.out.println("|||||||||||||||||||||||||||||||||||||");
      try {
        final JsonObject account = content.getAsJsonObject();
        final String id          = params.get(0).getValue();

        System.out.println("@id " + id);
        System.out.println("@account : "+account);

        final URL url            = new URL(String.format("http://localhost:8080/api/v1/account/%s?api_key=%s", id, token));

        return make_request_put_at_with(url, account);
      } catch (Exception e) {
        System.out.println("@/account/update path");
        System.out.println(e);
      }

      return null;
    }));


    // OPTIONS
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/config/all",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/auth",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/account/create",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/account/update",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/config/update",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/config/create",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/ping",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));
    xatkitBot.getXatkitServer().registerRestEndpoint(HttpMethod.OPTIONS, "/config/delete",
    RestHandlerFactory.createJsonRestHandler((headers, params, content) -> {
      JsonObject statusObject = new JsonObject();
      return statusObject;
    }));

  }
}
