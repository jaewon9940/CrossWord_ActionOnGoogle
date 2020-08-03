package com.o2o.action.server.app;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.google.api.services.actions_fulfillment.v2.model.HtmlResponse;
import com.google.api.services.actions_fulfillment.v2.model.SimpleResponse;
import com.o2o.action.server.util.CommonUtil;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class Main extends DialogflowApp {

  String URL = "https://actions.o2o.kr/devsvr7/test/index.html";

  GameBoard gameBoard;

  Set<String> hintOpenTp;
  Set<String> hintCloseTp;

  UserInfo user;
  GameInfo gameinfo;
  TTS tts;
    private void setUp() {
    gameinfo = new GameInfo();
      tts = new TTS();
  }

  @ForIntent("Default Welcome Intent")
  public ActionResponse defaultWelcome(ActionRequest request) throws ExecutionException, InterruptedException {
      ResponseBuilder rb = getResponseBuilder(request);
      Map<String, Object> data = rb.getConversationData();
      Map<String, Object> htmldata = new HashMap<>();
    HtmlResponse htmlResponse = new HtmlResponse();

    String response;
    data.clear();
    setUp();

    if (!request.hasCapability("actions.capability.INTERACTIVE_CANVAS")) {
      response = "Inveractive Canvas가 지원되지 않는 기기예요.";
      return rb.add(new SimpleResponse().setSsml(response)).endConversation().build();
    }
    else {
      htmldata.put("command", "welcome");
      response = "welcome display";
      System.out.println(response);
      //data.put("history","welcome");
      return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("welcome")))
              .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
              .build();
    }
  }
  @ForIntent("mainFromWelcome")
  public ActionResponse mainFromWelcome(ActionRequest request) throws ExecutionException, InterruptedException {
      return main(request);
  }


     @ForIntent("main")
    public ActionResponse main(ActionRequest request) throws ExecutionException, InterruptedException {
      ResponseBuilder rb = getResponseBuilder(request);
      Map<String, Object> data = rb.getConversationData();
      Map<String, Object> htmldata = new HashMap<>();
      HtmlResponse htmlResponse = new HtmlResponse();

    String response;

    user = new UserInfo();

    htmldata.put("command", "main");
    htmldata.put("level",user.getLevel());
    htmldata.put("myExp",user.getMyExp());
    htmldata.put("fullExp",user.getFullExp());
    htmldata.put("myHint",user.getMyHint());
    htmldata.put("myCoin",user.getMyCoin());

    //data.put("history","main");
    response = "mains display";
    return rb.add(new SimpleResponse().setTextToSpeech(response))
              .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
              .build();
  }


  @ForIntent("stageFromMain")
  public ActionResponse stageSelect(ActionRequest request) throws ExecutionException, InterruptedException {
      return stage(request);
  }
  @ForIntent("stageFromDifficulty")
  public ActionResponse stageFromDifficulty(ActionRequest request) throws ExecutionException, InterruptedException {
    return stage(request);
  }
  @ForIntent("stageFromResult")
  public ActionResponse stageFromResult(ActionRequest request) throws ExecutionException, InterruptedException {
    return stage(request);
  }

  public ActionResponse stage(ActionRequest request) throws ExecutionException, InterruptedException {
    ResponseBuilder rb = getResponseBuilder(request);
    Map<String, Object> data = rb.getConversationData();
    Map<String, Object> htmldata = new HashMap<>();
    HtmlResponse htmlResponse = new HtmlResponse();

    String response;

    htmldata.put("command", "stageSelect");

    data.put("history","stageSelect");
    response = "stageSelect display";
    return rb.add(new SimpleResponse().setTextToSpeech(response))
            .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
            .build();
  }


  @ForIntent("difficultyFromMain")
  public ActionResponse difficultySelect(ActionRequest request) throws ExecutionException, InterruptedException {
      return difficulty(request);
  }
  @ForIntent("difficultyFromStage")
  public ActionResponse difficultyFromStage(ActionRequest request) throws ExecutionException, InterruptedException {
    return difficulty(request);
  }
  public ActionResponse difficulty(ActionRequest request) throws ExecutionException, InterruptedException {
    ResponseBuilder rb = getResponseBuilder(request);
    Map<String, Object> data = rb.getConversationData();
    Map<String, Object> htmldata = new HashMap<>();
    HtmlResponse htmlResponse = new HtmlResponse();

    String response;

    /*
    //메인에서 왔는지,, 스테이지에서왔는지
      int stage;
      if(data.get("history")=="main") {
          stage = user.getLevel();
      }
      else {
          stage = ((Double)request.getParameter("number")).intValue();
      }
    data.put("stage",stage);
     */

      //나중에 자료구조로 코딩

    htmldata.put("command", "difficultySelect");
    htmldata.put("winMoney1",gameinfo.getStage()[1].getWinMoney());
    htmldata.put("winMoney2",gameinfo.getStage()[2].getWinMoney());
    htmldata.put("winMoney3",gameinfo.getStage()[3].getWinMoney());
    htmldata.put("betMoney1",gameinfo.getStage()[1].getBetMoney());
    htmldata.put("betMoney2",gameinfo.getStage()[2].getBetMoney());
    htmldata.put("betMoney3",gameinfo.getStage()[3].getBetMoney());
    htmldata.put("timeLimit1",gameinfo.getStage()[1].getTimeLimit()[0]);
    htmldata.put("timeLimit2",gameinfo.getStage()[2].getTimeLimit()[0]);
    htmldata.put("timeLimit3",gameinfo.getStage()[3].getTimeLimit()[0]);

    /*
    htmldata.put("winMoney", DB.getWinMoney(stage));
    htmldata.put("betMoney",DB.getBetMoney(stage));
    htmldata.put("timeLimit",DB.getTimeLimit(stage));
 */

    data.put("history","difficultySelect");
    response = "difficultySelect display";
    return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("difficultyselect")))
            .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
            .build();

  }

  @ForIntent("ingameFromDifficulty")
  public ActionResponse ingameFromDifficulty(ActionRequest request) throws ExecutionException, InterruptedException {
      return ingame(request);
  }
  @ForIntent("ingameFromResult")
  public ActionResponse ingameFromResult(ActionRequest request) throws ExecutionException, InterruptedException {
    return ingame(request);
  }

  public ActionResponse ingame(ActionRequest request) throws ExecutionException, InterruptedException {
    ResponseBuilder rb = getResponseBuilder(request);
    Map<String, Object> data = rb.getConversationData();
    Map<String, Object> htmldata = new HashMap<>();
    HtmlResponse htmlResponse = new HtmlResponse();

    String response;
    gameBoard = new GameBoard(GameBoard.Difficulty.easy,3);
    //gameBoard = new GameBoard(data.difficulty,data.stage);
    char[][] board = gameBoard.getBoard();
    int timeLimit = gameBoard.getTimeLimit();
    int totalWord = gameBoard.getTotalWord();
    htmldata.put("command", "ingame");
    htmldata.put("board",board);
    htmldata.put("timeLimit",timeLimit);
    htmldata.put("totalWord",totalWord);

    //data.put("history","ingame");
    response = "ingame display";
    return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("ingame")))
            .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
            .build();
  }

  @ForIntent("ingameMessage")
  public ActionResponse ingameMessage(ActionRequest request) throws ExecutionException, InterruptedException {
    ResponseBuilder rb = getResponseBuilder(request);
    Map<String, Object> data = rb.getConversationData();
    Map<String, Object> htmldata = new HashMap<>();
    HtmlResponse htmlResponse = new HtmlResponse();

    String response ="";
    String hint = CommonUtil.makeSafeString(request.getParameter("hint"));
    if(hint != null) {
      if(hint.equals("open")){
        htmldata.put("command","openHint");
        htmldata.put("hint",gameBoard.getHintMessage());
        response = "open hint";
      }
      else{
        htmldata.put("command","closeHint");
        response = "close hint";
      }
    }
    else {
      String word = CommonUtil.makeSafeString(request.getParameter("any"));
      if(gameBoard.tryAnswer(word)){
        htmldata.put("command","correct");
        response = "correct";
        Result result=gameBoard.getResult();

        if(result.isWin()) {
          htmldata.put("finish", true);
          rb.removeContext("ingameMessage");
        }
        else
          htmldata.put("finish",false);
      }
      else {
        htmldata.put("command", "wrong");
        response = "wrong";
      }
    }

    return rb.add(new SimpleResponse().setTextToSpeech(response))
            .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
            .build();

  }

  @ForIntent("result")
  public ActionResponse result(ActionRequest request) throws ExecutionException, InterruptedException {
    ResponseBuilder rb = getResponseBuilder(request);
    Map<String, Object> data = rb.getConversationData();
    Map<String, Object> htmldata = new HashMap<>();
    HtmlResponse htmlResponse = new HtmlResponse();

    String response;

    String result = CommonUtil.makeSafeString(request.getParameter("result"));


    Result results = gameBoard.getResult();
    htmldata.put("level",user.getLevel());
    htmldata.put("myExp",user.getMyExp());
    htmldata.put("fullExp",user.getFullExp());
    htmldata.put("myHint",user.getMyHint());
    htmldata.put("myCoin",user.getMyCoin());
    htmldata.put("correctList",results.getAnser());
    htmldata.put("wrongList",results.getRestWord());

    htmldata.put("command", "result");
    htmldata.put("result",result);

    //data.put("history","result");
    response = "result display";
    return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("win")))
            .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
            .build();
  }

}
