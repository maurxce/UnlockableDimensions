package me.maurxce.paidportals.dependency.optional;

import md.schorn.spigothelper.dependency.Hook;
import md.schorn.spigothelper.dependency.SimpleHook;

@Hook("PlaceholderAPI")
public class PlaceholderMiddleware extends SimpleHook {

    @Override
    public void init() {
        new PlaceholderHook().init();
    }
}
