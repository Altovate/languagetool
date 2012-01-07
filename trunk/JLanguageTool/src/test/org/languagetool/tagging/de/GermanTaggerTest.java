/* LanguageTool, a natural language style checker 
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.tagging.de;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;

import junit.framework.TestCase;
import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;

/**
 * @author Daniel Naber
 */
public class GermanTaggerTest extends TestCase {

  public void testTagger() throws IOException {
    final GermanTagger tagger = new GermanTagger();
    
    AnalyzedGermanTokenReadings aToken = tagger.lookup("Haus");
    assertEquals("Haus[Haus/SUB:AKK:SIN:NEU, Haus/SUB:DAT:SIN:NEU, Haus/SUB:NOM:SIN:NEU]", aToken.toSortedString());
    assertEquals("Haus", aToken.getReadings().get(0).getLemma());
    assertEquals("Haus", aToken.getReadings().get(1).getLemma());
    assertEquals("Haus", aToken.getReadings().get(2).getLemma());
    
    aToken = tagger.lookup("Hauses");
    assertEquals("Hauses[Haus/SUB:GEN:SIN:NEU]", aToken.toSortedString());
    assertEquals("Haus", aToken.getReadings().get(0).getLemma());
    
    aToken = tagger.lookup("hauses");
    assertNull(aToken);
    
    aToken = tagger.lookup("Groß");
    assertNull(aToken);
    
    aToken = tagger.lookup("großer");
    assertEquals("großer[groß/ADJ:DAT:SIN:FEM:GRU:SOL, groß/ADJ:GEN:PLU:FEM:GRU:SOL, groß/ADJ:GEN:PLU:MAS:GRU:SOL, " +
            "groß/ADJ:GEN:PLU:NEU:GRU:SOL, groß/ADJ:GEN:SIN:FEM:GRU:SOL, groß/ADJ:NOM:SIN:MAS:GRU:IND, " +
            "groß/ADJ:NOM:SIN:MAS:GRU:SOL]", aToken.toSortedString());
    assertEquals("groß", aToken.getReadings().get(0).getLemma());
    
    // from both german.dict and added.txt:
    aToken = tagger.lookup("Interessen");
    assertEquals("Interessen[Interesse/SUB:AKK:PLU:NEU, Interesse/SUB:DAT:PLU:NEU, " +
            "Interesse/SUB:GEN:PLU:NEU, Interesse/SUB:NOM:PLU:NEU]",
        aToken.toSortedString());
    assertEquals("Interesse", aToken.getReadings().get(0).getLemma());
    assertEquals("Interesse", aToken.getReadings().get(1).getLemma());
    assertEquals("Interesse", aToken.getReadings().get(2).getLemma());
    assertEquals("Interesse", aToken.getReadings().get(3).getLemma());
    
    // words that are not in the dictionary but that are recognized thanks to noun splitting:
    aToken = tagger.lookup("Donaudampfschiff");
    assertEquals("Donaudampfschiff[Donaudampfschiff/SUB:AKK:SIN:NEU, Donaudampfschiff/SUB:DAT:SIN:NEU, " +
            "Donaudampfschiff/SUB:NOM:SIN:NEU]", aToken.toSortedString());
    assertEquals("Donaudampfschiff", aToken.getReadings().get(0).getLemma());
    assertEquals("Donaudampfschiff", aToken.getReadings().get(1).getLemma());
    
    aToken = tagger.lookup("Häuserkämpfe");
    assertEquals("Häuserkämpfe[Häuserkampf/SUB:AKK:PLU:MAS, Häuserkampf/SUB:GEN:PLU:MAS, Häuserkampf/SUB:NOM:PLU:MAS]",
        aToken.toSortedString());
    assertEquals("Häuserkampf", aToken.getReadings().get(0).getLemma());
    assertEquals("Häuserkampf", aToken.getReadings().get(1).getLemma());
    assertEquals("Häuserkampf", aToken.getReadings().get(2).getLemma());
    
    aToken = tagger.lookup("Häuserkampfes");
    assertEquals("Häuserkampfes[Häuserkampf/SUB:GEN:SIN:MAS]", aToken.toSortedString());
    assertEquals("Häuserkampf", aToken.getReadings().get(0).getLemma());
    
    aToken = tagger.lookup("Häuserkampfs");
    assertEquals("Häuserkampfs[Häuserkampf/SUB:GEN:SIN:MAS]", aToken.toSortedString());
    assertEquals("Häuserkampf", aToken.getReadings().get(0).getLemma());

    aToken = tagger.lookup("Lieblingsfarben");
    assertEquals("Lieblingsfarben[Lieblingsfarbe/SUB:AKK:PLU:FEM, Lieblingsfarbe/SUB:DAT:PLU:FEM, " +
            "Lieblingsfarbe/SUB:GEN:PLU:FEM, Lieblingsfarbe/SUB:NOM:PLU:FEM]", aToken.toSortedString());
    assertEquals("Lieblingsfarbe", aToken.getReadings().get(0).getLemma());

    aToken = tagger.lookup("Autolieblingsfarben");
    assertEquals("Autolieblingsfarben[Autolieblingsfarbe/SUB:AKK:PLU:FEM, Autolieblingsfarbe/SUB:DAT:PLU:FEM, " +
            "Autolieblingsfarbe/SUB:GEN:PLU:FEM, Autolieblingsfarbe/SUB:NOM:PLU:FEM]", aToken.toSortedString());
    assertEquals("Autolieblingsfarbe", aToken.getReadings().get(0).getLemma());

    aToken = tagger.lookup("übrigbleibst");
    assertEquals("übrigbleibst[übrigbleiben/VER:2:SIN:PRÄ:NON:NEB]", aToken.toSortedString());
    assertEquals("übrigbleiben", aToken.getReadings().get(0).getLemma());
  }

  // make sure we use the version of the POS data that was extended with post spelling reform data
  public void testExtendedTagger() throws IOException {
    final GermanTagger tagger = new GermanTagger();

    assertEquals("Kuß[Kuß/SUB:AKK:SIN:MAS, Kuß/SUB:DAT:SIN:MAS, Kuß/SUB:NOM:SIN:MAS]", tagger.lookup("Kuß").toSortedString());
    assertEquals("Kuss[Kuss/SUB:AKK:SIN:MAS, Kuss/SUB:DAT:SIN:MAS, Kuss/SUB:NOM:SIN:MAS]", tagger.lookup("Kuss").toSortedString());

    assertEquals("Haß[Haß/SUB:AKK:SIN:MAS, Haß/SUB:DAT:SIN:MAS, Haß/SUB:NOM:SIN:MAS]", tagger.lookup("Haß").toSortedString());
    assertEquals("Hass[Hass/SUB:AKK:SIN:MAS, Hass/SUB:DAT:SIN:MAS, Hass/SUB:NOM:SIN:MAS]", tagger.lookup("Hass").toSortedString());

    assertEquals("muß[müssen/VER:MOD:1:SIN:PRÄ, müssen/VER:MOD:3:SIN:PRÄ]", tagger.lookup("muß").toSortedString());
    assertEquals("muss[müssen/VER:MOD:1:SIN:PRÄ, müssen/VER:MOD:3:SIN:PRÄ]", tagger.lookup("muss").toSortedString());
  }

  public void testTaggerBaseforms() throws IOException {
    final GermanTagger tagger = new GermanTagger();
    
    List<AnalyzedGermanToken> readings = tagger.lookup("übrigbleibst").getGermanReadings();
    assertEquals(1, readings.size());
    assertEquals("übrigbleiben", readings.get(0).getLemma());

    readings = tagger.lookup("Haus").getGermanReadings();
    assertEquals(3, readings.size());
    assertEquals("Haus", readings.get(0).getLemma());
    assertEquals("Haus", readings.get(1).getLemma());
    assertEquals("Haus", readings.get(2).getLemma());

    readings = tagger.lookup("Häuser").getGermanReadings();
    assertEquals(3, readings.size());
    assertEquals("Haus", readings.get(0).getLemma());
    assertEquals("Haus", readings.get(1).getLemma());
    assertEquals("Haus", readings.get(2).getLemma());
  }

  public void testTag() throws IOException {
    final GermanTagger tagger = new GermanTagger();

    final List<String> upperCaseWord = new ArrayList<String>();
    upperCaseWord.add("Das");

    List<AnalyzedTokenReadings> readings = tagger.tag(upperCaseWord);
    assertEquals("[Das[der/ART:DEF:AKK:SIN:NEU, der/ART:DEF:NOM:SIN:NEU, der/PRO:DEM:AKK:SIN:NEU, " +
            "der/PRO:DEM:NOM:SIN:NEU, der/PRO:PER:AKK:SIN:NEU, der/PRO:PER:NOM:SIN:NEU]]", readings.toString());
    
    readings = tagger.tag(upperCaseWord, false);
    assertEquals("[Das[null/null]]", readings.toString());
  }
  
  public void testDictionary() throws IOException {
    final Dictionary dictionary = Dictionary.read(
        JLanguageTool.getDataBroker().getFromResourceDirAsUrl("/de/german.dict"));
    final DictionaryLookup dl = new DictionaryLookup(dictionary);
    for (WordData wd : dl) {
      if (wd.getTag() == null || wd.getTag().length() == 0) {
        System.err.println("**** Warning: the word " + wd.getWord() + "/" + wd.getStem()
                + " lacks a POS tag in the dictionary.");
      }
    }    
  }
  
}
