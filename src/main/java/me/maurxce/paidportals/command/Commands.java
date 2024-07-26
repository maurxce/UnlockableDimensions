package me.maurxce.paidportals.command;

import lombok.Getter;

@Getter
public enum Commands {
    RELOAD("paidportals.reload", "reload"),
    RESET("paidportals.reset", "reset"),
    PAY("paidportals.pay", "pay", "<amount>"),
    BALANCE("paidportals.balance", "balance");

    private final String permission;
    private final String[] arguments;

    Commands(String permission, String... arguments) {
        this.permission = permission;
        this.arguments = arguments;
    }
}
