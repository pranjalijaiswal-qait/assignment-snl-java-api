package com.qainfotech.tap.training.snl.api;

import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class BoardTest {
	Board board;
  @BeforeTest
  public void beforeTest() throws FileNotFoundException, UnsupportedEncodingException, IOException
  {
	  board=new Board();
  
  }
  @BeforeMethod
  public void initial() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
  {
	  board=new Board();
	  board.registerPlayer("Pranjali");
	  board.registerPlayer("Meenal");
	  board.registerPlayer("Pooja");
  }
  @Test
  public void registerPlayer() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
	  Assert.assertEquals(board.getData().getJSONArray("players").length(),3);
	  Assert.assertEquals(board.registerPlayer("Shreya").length(),4);
	  Assert.assertEquals(board.getData().getJSONArray("players").getJSONObject(1).get("name"), "Meenal");
	  Assert.assertEquals(board.getData().getJSONArray("players").getJSONObject(1).get("position"), 0);
  }
  @Test(expectedExceptions = MaxPlayersReachedExeption.class)
  public void registerPlayer_MaxPlayersReachedExeption() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
	  board.registerPlayer("Shreya");
	  board.registerPlayer("Arpit");
	  Assert.assertEquals(board.getData().getJSONArray("players").length(), 4);
  }
  @Test(expectedExceptions = PlayerExistsException.class)
  public void registerPlayer_PlayerExistsException() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
	  board.registerPlayer("Pranjali");
	  Assert.assertEquals(board.getData().getJSONArray("players").length(), 3);

  }
  @Test
  public void deletePlayer() throws NoUserWithSuchUUIDException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {
	  UUID u=(UUID) board.getData().getJSONArray("players").getJSONObject(0).get("uuid");
	  board.deletePlayer(u); 
	  Assert.assertEquals(board.getData().getJSONArray("players").length(), 2);
	    }
  @Test(expectedExceptions = NoUserWithSuchUUIDException.class)
  public void deletePlayer_NoUserWithSuchUUIDException() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException {
	  board.deletePlayer(UUID.fromString("6dc80cdf-b40d-4e72-9f30-a45e0d20c3d4"));
	  Assert.assertEquals(board.getData().getJSONArray("players").length(), 2);
	
  }
  @Test(expectedExceptions = InvalidTurnException.class)
  public void rollDie_InvalidTurnException() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {
	  int turn=board.data.getInt("turn");
	  board.rollDice(UUID.fromString("6dc80cdf-b40d-4e72-9f30-a45e0d20c3d4"));
	  Assert.assertEquals(board.data.getInt("turn"), turn);
  }
  @Test(expectedExceptions = GameInProgressException.class)
  public void registerplayer_GameInProgressException() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {
	 board.rollDice((UUID) board.data.getJSONArray("players").getJSONObject(board.data.getInt("turn")).get("uuid"));
	 board.registerPlayer("Arpit");
	 Assert.assertEquals(board.data.getJSONArray("players").length(),3);
  }
  @Test
  public void rollDice() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException 
  {

	    board.registerPlayer("Shreya");
		JSONArray ar=board.getData().getJSONArray("steps");
	    JSONArray arr=board.getData().getJSONArray("players");
	    JSONObject ob;
	    int turn;
	    turn=board.data.getInt("turn");
	    int initialpos=(int) arr.getJSONObject(turn).get("position");
		UUID u=(UUID) board.getData().getJSONArray("players").getJSONObject(turn).get("uuid");
		ob=board.rollDice(u);
		int finalpos=(int) arr.getJSONObject(turn).get("position");
		String msg=(String) ob.get("message");
		int type=(int)ar.getJSONObject(finalpos).get("type");
		 if(type==0)
		 {
			 Assert.assertEquals(msg, "Player moved to "+finalpos);
			 Assert.assertEquals(finalpos, (int)ob.get("dice")+initialpos);
		 }
		 else if(type==1)
		 {
			 Assert.assertEquals(msg, "Player was bit by a snake, moved back to "+finalpos);
			 Assert.assertEquals(((finalpos<initialpos)?true:false), true);
		 }
		 else if(type==2)
		 {
			 Assert.assertEquals(msg,"Player climbed a ladder, moved to "+finalpos);
			 Assert.assertEquals(((finalpos>initialpos)?true:false), true);
		 }
		 else
		 {
			Assert.assertEquals(initialpos, finalpos);
		 }
		    turn=board.data.getInt("turn");
			
	 }
	 

	}
