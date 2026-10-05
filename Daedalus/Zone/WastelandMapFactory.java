package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;

public class WastelandMapFactory extends MapFactory implements ZoneConstants, GUIConstants
{
   public static final int DEFAULT_WIDTH = 60;
   public static final int DEFAULT_HEIGHT = 50;
   
   public static ZoneMap getBasicMap()
   {
      ZoneMap map = new ZoneMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      map.setName("Wasteland");
      map.setMaxThreat(9);
      map.setMinThreat(3);
      map.setLevel(1);
      setJaggedBorder(map, 3, new ZoneTile(ZoneConstants.TileBase.WALL));
      
      for(int x = 0; x < 3; x++)
      for(int y = 0; y < 3; y++)
      {
         if(RNG.nextDouble() < .5)
         {
            int startingX = (x * (DEFAULT_WIDTH / 3)) + 1;
            int startingY = (y * (DEFAULT_HEIGHT / 3)) + 1;
            int featureWidth = 4 + RNG.nextInt(3);
            int featureHeight = 4 + RNG.nextInt(3);
            int plusX = RNG.nextInt((DEFAULT_WIDTH / 3) - featureWidth - 2);
            int plusY = RNG.nextInt((DEFAULT_HEIGHT / 3) - featureHeight - 2);
            startingX += plusX;
            startingY += plusY;
            
            switch(RNG.nextInt(6))
            {
               case 0:  addScatter(map, startingX, startingY, featureWidth, featureHeight, .2, 
                           new ZoneTile(ZoneConstants.TileBase.WALL));
                        break;
               case 1:  addScatter(map, startingX, startingY, featureWidth, featureHeight, .4, 
                           new ZoneTile(ZoneConstants.TileBase.WALL));
                        break;
               case 2:  addScatter(map, startingX, startingY, featureWidth, featureHeight, .2, 
                           new ZoneTile(ZoneConstants.TileBase.LOW_WALL));
                        break;
               case 3:  addScatter(map, startingX, startingY, featureWidth, featureHeight, .4, 
                           new ZoneTile(ZoneConstants.TileBase.LOW_WALL));
                        break;
               case 4:  addScatter(map, startingX, startingY, featureWidth, featureHeight, .1, 
                           new ZoneTile(ZoneConstants.TileBase.LOW_WALL));
                        addScatter(map, startingX, startingY, featureWidth, featureHeight, .1, 
                           new ZoneTile(ZoneConstants.TileBase.WALL));
               case 5:  addScatter(map, startingX, startingY, featureWidth, featureHeight, .2, 
                           new ZoneTile(ZoneConstants.TileBase.LOW_WALL));
                        addScatter(map, startingX, startingY, featureWidth, featureHeight, .2, 
                           new ZoneTile(ZoneConstants.TileBase.WALL));
            }
         }
         
         int randomRocks = RNG.nextInt(7) + 7;
         for(int i = 0; i < randomRocks; i++)
         {
            switch(RNG.nextInt(3))
            {
               case 0 : addRandomTile(map, new ZoneTile(ZoneConstants.TileBase.WALL)); break;
               case 1 : addRandomTile(map, new ZoneTile(ZoneConstants.TileBase.LOW_WALL)); break;
               case 2 : addRandomTile(map, new ZoneTile(ZoneConstants.TileBase.LOW_WALL)); break;
            }
         }
      }
         
      int randomCrates = RNG.nextInt(6) + 5;
      MapFactory.addCrates(map, 5, 5, map.getWidth() - 10, map.getHeight() - 10, randomCrates);
      
      fillUnreachable(map, new ZoneTile(ZoneConstants.TileBase.WALL));
      // not painted as we want to add exits first
      return map;
   }
   
   public static ZoneMap getHideout()
   {
      String[] tileArr =  {"#######################",
                           "############....#######",
                           "######...|...###..#####",
                           "#####!...#!######...U##",
                           "######...##############",
                           "####!#/################",
                           "#.........#############",
                           "#.........#############",
                           "#.........####........#",
                           "#.....................#",
                           "#.........####........#",
                           "#.........####........#",
                           "#.........####........#",
                           "####\\#########........#",
                           "####.....#####........#",
                           "####.....#####........#",
                           "####.....##############",
                           "#######################"};
   
      int width = tileArr[0].length();
      int height = tileArr.length;
      
      ZoneMap map = new ZoneMap(width, height);
      map.setName("Hideout");
      map.setMaxThreat(0);
      map.setMinThreat(0);
      map.setLevel(0);
      
      ZoneTile zt = null;
      int switchVal = EngineTools.getUniqueNum();
      Coord airlockDoor1 = null;
      Coord airlockDoor2 = null;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         switch(tileArr[y].charAt(x))
         {
            case '#' :  zt = new ZoneTile(TileBase.WALL);
                        break;
            case '.' :  zt = new ZoneTile(TileBase.CLEAR);
                        break;
            case 'U' :  zt = new Exit('U');
                        break;
            case '!' :  Switch s = new Switch();
                        s.setTriggerIndex(switchVal);
                        s.setTransparent(false);
                        zt = s;
                        break;
            case '/' :  Door d1 = new Door();
                        d1.setLocked(true);
                        d1.toggle();
                        zt = d1;
                        airlockDoor1 = new Coord(x, y);
                        break;
            case '|' :  Door d2 = new Door();
                        d2.setLocked(true);
                        zt = d2;
                        airlockDoor2 = new Coord(x, y);
                        break;
            case '\\' : zt = new Door();
                        break;
         }
         map.setTile(x, y, zt);
      }
      
      map.setExitList();
      
      map.addEventTrigger(new EventTrigger(switchVal, airlockDoor1, EventTrigger.TriggerAction.TOGGLE));
      map.addEventTrigger(new EventTrigger(switchVal, airlockDoor2, EventTrigger.TriggerAction.TOGGLE));
      MapPainter.paintWastelandOverworld(map);
      
      
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         if(map.getTile(x, y).getTileIndex() != '#')
            map.getTile(x, y).setBGColor(GREY);
      }
      
      for(int x = airlockDoor2.x + 1; x < width; x++)
      for(int y = 0; y < airlockDoor2.y + 4; y++)
      {
         if(map.getTile(x, y).getTileIndex() != '#')
            map.getTile(x, y).setBGColor(WASTELAND_GROUND_BG);
      }
      return map;
   }

}