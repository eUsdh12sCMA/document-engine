package com.docengine.app;

import java.util.Map;

// Decides the family from outside: command line first, then environment, then default
public final class FamilySelector {
    public static final String DEFAULT = "pdf";

    private FamilySelector() {}

    public static String choose(String[] args) { return choose(args, System.getenv()); }

    // env is a parameter so I can test it without touching the real environment
    public static String choose(String[] args, Map<String, String> env) {
        for (String arg : args) {
            if (arg.startsWith("family=")) return arg.substring("family=".length());
        }
        return env.getOrDefault("DOC_FAMILY", DEFAULT);
    }
}
