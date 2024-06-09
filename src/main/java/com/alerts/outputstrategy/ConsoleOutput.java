package com.alerts.outputstrategy;

public class ConsoleOutput implements AlertOutputStrategy {
    @Override
    public void send(String message) {
        System.out.println(message);
    }
}
