package com.alerts.outputstrategy;

public class GlobalOutput {
    private static AlertOutputStrategy instance;

    private GlobalOutput() {
        // private constructor to prevent instantiation
    }

    public static synchronized AlertOutputStrategy getInstance() {
        if (instance == null) {
            // default to console output strategy, can be changed later
            instance = new ConsoleOutput();
        }
        return instance;
    }

    public static synchronized void setInstance(AlertOutputStrategy outputStrategy) {
        instance = outputStrategy;
    }
}
