package com.example.contactappai;

public class App {
    private String name;
    private String apkPath;

    public App(String name, String apkPath) {
        this.name = name;
        this.apkPath = apkPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
