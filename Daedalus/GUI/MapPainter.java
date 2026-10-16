package Daedalus.GUI;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.Zone.*;

public class MapPainter implements ZoneConstants, GUIConstants
{
   public static NoiseChoir noise;
   
   public static void paintWastelandOverworld(ZoneMap map)
   {
      double noiseStepSize = .2;
      paintAllFloor(map, BLACK, WASTELAND_GROUND_BG);
      map.getOOBTile().setFGColor(STONE);
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         // tiles with the FG already set are ignored
         if(map.getTile(x, y).getFGColor() == WHITE)
         {
            switch(TileBase.getByTileIndex(map.getTile(x, y).getTileIndex()))
            {
               case CLEAR :
               case ROUGH :   map.getTile(x, y).setFGColor(WASTELAND_GROUND_FG);
                              break;
               case WALL :
               case LOW_WALL: 
                              map.getTile(x, y).setFGColor(STONE);
                              break;
            }
         }
      }
      applyNoiseToFloor(map, noiseStepSize);
   }
   
   
   public static void paintAllFloor(ZoneMap map, int color, boolean applyNoise, double noiseStepSize)
   {
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         map.getTile(x, y).setBGColor(color);
      }
      map.getOOBTile().setBGColor(color);
      if(applyNoise)
         applyNoiseToFloor(map, noiseStepSize);
   }
   public static void paintAllFloor(ZoneMap map, int color){paintAllFloor(map, color, false, 0.0);}
   
   // paint all floor where existing color matches oldColor
   public static void paintAllFloor(ZoneMap map, int oldColor, int newColor, boolean applyNoise, double noiseStepSize)
   {
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(map.getTile(x, y).getBGColor() == oldColor)
            map.getTile(x, y).setBGColor(newColor);
      }
      if(map.getOOBTile().getBGColor() == oldColor)
         map.getOOBTile().setBGColor(newColor);
      if(applyNoise)
         applyNoiseToFloor(map, noiseStepSize);
   }
   public static void paintAllFloor(ZoneMap map, int oldColor, int newColor){paintAllFloor(map, oldColor, newColor, false, 0.0);}
   
   
   public static void applyNoiseToFloor(ZoneMap map, double noiseStepSize)
   {
      noise = new NoiseChoir();
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         double adj = noise.getValue(x * noiseStepSize, y * noiseStepSize) / 3.0;
         map.getTile(x, y).setBGColor(GUITools.dimColor(map.getTile(x, y).getBGColor(), adj));
      }
   }
}