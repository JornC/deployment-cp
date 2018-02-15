package nl.yogh.aerius.wui.util;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.CompositionType;

public class UglyDuckling {
  private static ArrayList<CompositionType> compositions;

  public static ArrayList<CompositionType> compositions() {
    return compositions;
  }

  public static void compositions(final ArrayList<CompositionType> compositions) {
    UglyDuckling.compositions = compositions;
  }
}
