package Daedalus.GUI;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Daedalus.Zone.*;
import WidlerSuite.Coord;
import WidlerSuite.NoiseChoir;
import WidlerSuite.WSFontConstants;

public class MapPainter implements ZoneConstants, GUIConstants
{
   public static NoiseChoir noise;
   
   public static void paintWastelandOverworld(ZoneMap map)
   {
      noise = new NoiseChoir();
      double noiseStepSize = .2;
      paintAllFloor(map, WASTELAND_GROUND_BG, true, noiseStepSize);
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         // tiles with the FG already set are ignored
         if(map.getTile(x, y).getFGColor() == WHITE)
         {
            double adj = noise.getValue(x * noiseStepSize, y * noiseStepSize) / 3.0;
            switch(TileBase.getByTileIndex(map.getTile(x, y).getTileIndex()))
            {
               case CLEAR :
                  map.getTile(x, y).setFGColor(GUITools.dimColor(WASTELAND_GROUND_FG, adj));
                  break;
               case WALL :
               case LOW_WALL : 
                  map.getTile(x, y).setFGColor(GUITools.dimColor(STONE, adj));
            }
         }
      }
   }
   
   public static void paintAllFloor(ZoneMap map, int color, boolean applyNoise, double noiseStepSize)
   {
      if(applyNoise)
         noise = new NoiseChoir();
      for(int x = 0; x < map.getWidth(); x++)
      for(int y = 0; y < map.getHeight(); y++)
      {
         if(applyNoise)
         {
            double adj = noise.getValue(x * noiseStepSize, y * noiseStepSize) / 3.0;
            map.getTile(x, y).setBGColor(GUITools.dimColor(color, adj));
         }
         else
            map.getTile(x, y).setBGColor(color);
      }
   }
}