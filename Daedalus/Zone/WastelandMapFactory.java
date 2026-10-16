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
   
   public static ZoneMap getUnpaintedBasicMap(int level, int exits)
   {
      ZoneMap map = new ZoneMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      map.setName("Wasteland");
      map.setMaxThreat(9);
      map.setMinThreat(3);
      map.setLevel(level);
      setJaggedBorder(map, 3, new ZoneTile(ZoneConstants.TileBase.WALL));
      
      if((exits & BITMASK_NORTH) == BITMASK_NORTH)
         addRandomExit(map, 'N');
      if((exits & BITMASK_EAST) == BITMASK_EAST)
         addRandomExit(map, 'E');
      if((exits & BITMASK_SOUTH) == BITMASK_SOUTH)
         addRandomExit(map, 'S');
      if((exits & BITMASK_WEST) == BITMASK_WEST)
         addRandomExit(map, 'W');
      // hideout entrance
      if((exits & BITMASK_DOWN) == BITMASK_DOWN)
      {
         addRandomExit(map, 'S');
         Exit hideoutEntrance = map.getExitByDirection(ExitDirection.SOUTH);
         hideoutEntrance.setExitDirection('D');
      }

      for(int x = 0; x < 3; x++)
      for(int y = 0; y < 3; y++)
      {
         // random clusters
         if(RNG.nextDouble() < .75)
         {
            int startingX = (x * (DEFAULT_WIDTH / 3)) + 1;
            int startingY = (y * (DEFAULT_HEIGHT / 3)) + 1;
            int featureWidth = 3 + RNG.nextInt(3);
            int featureHeight = 3 + RNG.nextInt(3);
            int plusX = RNG.nextInt((DEFAULT_WIDTH / 3) - featureWidth - 2);
            int plusY = RNG.nextInt((DEFAULT_HEIGHT / 3) - featureHeight - 2);
            startingX += plusX;
            startingY += plusY;
            if(x == 0)
               startingX = Math.max(startingX, 4);
            if(y == 0)
               startingY = Math.max(startingY, 4);
            if(x == 2)
               startingX = Math.min(startingX, DEFAULT_WIDTH - (featureWidth + 4));
            if(y == 2)
               startingY = Math.min(startingY, DEFAULT_HEIGHT - (featureHeight + 4));
            int halfMaxCount = (featureWidth * featureHeight) / 2;
            int count = RNG.nextInt(halfMaxCount) + halfMaxCount;
            addTightScatter(map, startingX, startingY, featureWidth, featureHeight, count + RNG.nextInt(4), 
                            new ZoneTile(ZoneConstants.TileBase.LOW_WALL));
            addTightScatter(map, startingX, startingY, featureWidth, featureHeight, count, 
                            new ZoneTile(ZoneConstants.TileBase.WALL));
         }
         
         // random individual rocks
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
      map.setExitList();
      
      return map;
   }
   
   public static ZoneMap getBasicMap(int level, int exits)
   {
      ZoneMap map = getUnpaintedBasicMap(level, exits);
      
      MapPainter.paintWastelandOverworld(map);
      
      return map;
   }
   
   public static void setCrashedShip(ZoneMap map)
   {
      String[] tileArr =  {"           1111111      ",
                           "         122###|####    ",
                           "       12233#?c.ccc##   ",
                           "     1223333#?c....c##  ",
                           "   122333344###|####### ",
                           "112233444455###.......::",
                           "112233444455###.......::",
                           "   122333344###/####### ",
                           "     1223333#?c....c##  ",
                           "       12233#?c.ccc##   ",
                           "         122###/####    ",
                           "           1111111      "};
      int width = tileArr[0].length();
      int height = tileArr.length;
      int yStart = 4 + RNG.nextInt(4);
      int xStart = 5 + RNG.nextInt(map.getWidth() - (width + 10));
      
      int baseScorchColor = GUITools.dimColor(WASTELAND_GROUND_BG, .33);
      int[] scorchArr = GUITools.getColorGradient(baseScorchColor, BLACK, 6);
      
      ZoneTile zt = null;
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         switch(tileArr[y].charAt(x))
         {
            case '#' :  zt = new ZoneTile(TileBase.WALL);
                        zt.setBGColor(GREY);
                        zt.setFGColor(METAL);
                        zt.setName("Crashed Ship");
                        break;
            case '.' :  zt = new ZoneTile(TileBase.CLEAR);
                        zt.setBGColor(GREY);
                        break;
            case '1' :  ;
            case '2' :  ;
            case '3' :  ;
            case '4' :  ;
            case '5' :  zt = new ZoneTile(TileBase.ROUGH);
                        zt.setBGColor(scorchArr[tileArr[y].charAt(x) - '1']);
                        zt.setFGColor(WASTELAND_GROUND_FG);
                        break;
            case ':' :  zt = new ZoneTile(TileBase.BARS);
                        zt.setBGColor(GREY);
                        zt.setName("Viewing Pane");
                        break;
            case '/' :  Door d1 = new Door();
                        d1.toggle();
                        zt = d1;
                        zt.setBGColor(GREY);
                        break;
            case '|' :  Door d2 = new Door();
                        zt = d2;
                        zt.setBGColor(GREY);
                        break;
            case 'c' :  zt = Chest.getCrate(1);
                        zt.setBGColor(GREY);
                        break;
            case '?' :  zt = new Chest(1);
                        zt.setBGColor(GREY);
                        break;
            case ' ' :  zt = null;
                        break;
         }
         if(zt != null)
            map.setTile(x + xStart, y + yStart, zt);
      }
      // break stuff
      for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
      {
         // break inner door in case of items dropping
         if(RNG.nextDouble() < .1 || y == 4 && tileArr[y].charAt(x) == '|')
         {
            switch(tileArr[y].charAt(x))
            {
               case '#' :  
               case ':' :
               case '/' :
               case '|' :
               case 'c' :
               case '.' :  map.attackTile(x + xStart, y + yStart, true);
                           break;
            }
         }
      }
   }
   
   public static ZoneMap getCrashedShipMap(int level, int exits)
   {
      ZoneMap map = getUnpaintedBasicMap(level, exits);
      setCrashedShip(map);
      MapPainter.paintWastelandOverworld(map);
      
      return map;
   }
   
   public static ZoneMap getHideout(int[][] mazeMap)
   {
      String[] tileArr =  {"#######################",
                           "############....#######",
                           "######...|...###..#####",
                           "#####!...#!######...U##",
                           "######...##############",
                           "####!#/################",
                           "#.........#############",
                           "#.........S############",
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
      Sign sign = null;
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
            case 'S' :  sign = new Sign();
                        sign.setWall(true);
                        sign.setText(mazeMap);
                        zt = sign;
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