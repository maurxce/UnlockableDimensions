package me.maurxce.paidportals.repository.contract;

import java.util.concurrent.CompletableFuture;

public interface Syncable {
    CompletableFuture<Void> sync();
}
