package Daedalus.Engine;

import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TextDictionary
{
   private Vector<TextDictObj> entryList;
   
   
   public TextDictionary()
   {
      entryList = new Vector<TextDictObj>();
   }
   
   public TextDictionary(String fileName)
   {
      this();
      load(fileName);
   }
   
   public String getEntry(String key)
   {
      key = key.toUpperCase();
      for(TextDictObj entry: entryList)
      {
         if(entry.key.equals(key))
            return entry.value;
      }
      return "";
   }
   
   
   public void load(String fileName)
   {
      File file = new File(fileName);
      try
		{
			Scanner inFile = new Scanner(file);
         TextDictObj curObj = null;
         String curStr = "";
			// main work loop
			while(inFile.hasNextLine())
			{
            curStr = inFile.nextLine();
            // new entry
            if(curStr.length() > 0 && curStr.charAt(0) == '@')
            {
               if(curObj != null)
               {
                  curObj.value = curObj.value.strip();
                  entryList.add(curObj);
               }
               curStr = curStr.substring(1, curStr.length());
               curObj = new TextDictObj(curStr.toUpperCase());
            }
            else
            {
               curObj.value += curStr + "\n";
            }
			}
			inFile.close();
         // register last entry
         if(curObj != null)
         {
            curObj.value = curObj.value.strip();
            entryList.add(curObj);
         }
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
   }
   
   private class TextDictObj
   {
      public String key;
      public String value;
      
      public TextDictObj(String str)
      {
         key = str;
         value = "";
      }
   }
   
   public static void main(String[] args)
   {
      TextDictionary dict = new TextDictionary();
      dict.load("Daedalus/res/Text/Weapons.txt");
      for(TextDictObj obj: dict.entryList)
      {
         System.out.println("Entry: " + obj.key);
         System.out.println(obj.value);
      }
   }
}