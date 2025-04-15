module Main {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.base;

    exports main;
    exports controller;
    exports model;
    exports view;
}