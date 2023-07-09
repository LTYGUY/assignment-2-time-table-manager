package com.example.timetable;

public class AllManagers {
    public static AllManagers Instance;
    //no point making it non static, need to write one more '.Instance'.
    public static NavigationManager NavigationManager;

    public AllManagers(){
        if (Instance != null)
            return;

        Instance = this;
        NavigationManager = new NavigationManager();
    }
}
