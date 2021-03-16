package com.example.hydroseed;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Global {

    static final int historySize = 5;
    public static List<CalculationObject> historyList = new ArrayList<>();
    public static LinkedHashSet<CalculationObject> dupe = new LinkedHashSet<>();
    public static List<CalculationObject> historyList2 = new ArrayList<>();

}
