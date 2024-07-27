package me.maurxce.paidportals.command;

import lombok.Getter;

import java.util.List;

@Getter
public enum Commands {
    RELOAD("paidportals.reload", "reload"),
    RESET("paidportals.reset", "reset"),
    PAY("paidportals.pay", "pay", "<amount>"),
    BALANCE("paidportals.balance", "balance"),
    GOAL("paidportals.goal", "goal");

    private final String permission;
    private final List<String> arguments;

    Commands(String permission, String... arguments) {
        this.permission = permission;
        this.arguments = List.of(arguments);
    }
}
