package com.example.hydroseed;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Global {

    static final double ACRE_TO_SQFT = 43560;

    static final int historySize = 5;
    public static List<CalculationObject> historyList = new ArrayList<>();


    static double[] applicationRates = {270, 65, 1000, 1700, 60, 2500, 175};
    // Step 1: Compost

    // Index 0 --> Compost Rate


    // Step 2: HydroSeed

    // Index 1 --> Seed Rate
    // Index 2 --> Fiber HydroSeed Rate
    // Index 3 --> Fertilizer Rate
    // Index 4 --> Additive Rate

    // Step 3: Hydromulch


    // Index 5 --> Fiber HydroMulch Rate
    // Index 6 --> Tackifier Rate

    static double userInputAcres;
    static double userInputSqft;
    static double tankSize;

}
