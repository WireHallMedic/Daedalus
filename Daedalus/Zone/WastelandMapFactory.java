package Daedalus.Zone;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.AI.*;
import Daedalus.GUI.*;
import Daedalus.Item.*;
import Daedalus.Actor.*;
import Daedalus.Engine.*;
import WidlerSuite.Coord;
import WidlerSuite.FloodFill;
import WidlerSuite.SpiralSearch;

public class WastelandMapFactory extends MapFactory implements ZoneConstants, GUIConstants
{
   public static final int DEFAULT_WIDTH = 60;
   public static final int DEFAULT_HEIGHT = 50;
   
   public static ZoneMap getBasicMap()
   {
      ZoneMap map = new ZoneMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      map.setName("Wasteland");
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
      
      fillUnreachable(map, new ZoneTile(ZoneConstants.TileBase.WALL));
      // not painted as we want to add exits first
      return map;
   }
   
   public static ZoneMap getHideout()
   {
      ZoneMap map = new ZoneMap(10, 10);
      map.setName("Hideout");
      setBorder(map, new ZoneTile(ZoneConstants.TileBase.WALL));
      map.setTile(1, 1, new Exit('U'));
      map.setExitList();
      
      return map;
   }

}