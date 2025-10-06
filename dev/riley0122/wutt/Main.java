package dev.riley0122.wutt;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static boolean Men1Patch = false;
    public static String Men1PatchPath = "";
    public static boolean Men2Patch = false;
    public static String Men2PatchPath = "";
    public static boolean cafeBaristaPatch = false;
    public static String cafeBaristaPatchPath = "";

    public static String themeName = "";
    public static String ip = "";

    enum LogLevel {
        FATAL(5),
        ERROR(4),
        WARN(3),
        INFO(2),
        DEBUG(1),
        BYTE(0);
    	
    	private Integer severity;
    	LogLevel(int severity) {
    		this.severity = severity;
    	}
    	
    	public boolean shouldShow(LogLevel other) {
    		return this.severity >= other.severity;
    	}
    }

    public static LogLevel logLevel = LogLevel.ERROR;

    public static void log(String message, LogLevel level) {
        if (level.shouldShow(logLevel)) {
            System.out.println("[" + level.name() + "] " + message);
        }
        
        if (level == LogLevel.FATAL) {
        	throw new RuntimeException();
    	}
    }

    public static void main(String[] args) {
        System.out.println("Wii U Theme Tool (WUTT) - v1.0.0");
        System.out.println("By Riley0122 (riley0122.dev)");

        for (String arg : args) {
            if (arg.toLowerCase().startsWith("--loglevel=")) {
                String level = arg.split("=")[1].toUpperCase();
                try {
                    logLevel = LogLevel.valueOf(level);
                    log("Log level set to " + level, LogLevel.INFO);
                } catch (IllegalArgumentException e) {
                    log("Invalid log level: " + level + ". Defaulting to ERROR.", LogLevel.ERROR);
                }
            }
        }

        System.out.println("What is the IP address of you Wii U console?");
        ip = System.console().readLine().trim();
        if (ip.isBlank()) {
            log("IP address cannot be empty. Exiting.", LogLevel.FATAL);
            return;
        }

        System.out.println("What is the name of this theme?");
        themeName = System.console().readLine().trim();

        System.out.println("Is there a Men.bps file you want to apply? (y/N)");
        String input = System.console().readLine().trim().toLowerCase();
        Men1Patch = input.equals("y") || input.equals("yes");

        if (Men1Patch) {
            System.out.println("What is the path of the Men.bps file?");
            Men1PatchPath = System.console().readLine().trim().replaceAll("\"", "");
        }

        System.out.println("Is there a Men2.bps file you want to apply? (y/N)");
        input = System.console().readLine().trim().toLowerCase();
        Men2Patch = input.equals("y") || input.equals("yes");

        if (Men2Patch) {
            System.out.println("What is the path of the Men2.bps file?");
            Men2PatchPath =  System.console().readLine().trim().replaceAll("\"", "");
        }

        System.out.println("Is there a cafe_barista_men.bps file you want to apply? (y/N)");
        input = System.console().readLine().trim().toLowerCase();
        cafeBaristaPatch = input.equals("y") || input.equals("yes");

        if (cafeBaristaPatch) {
            System.out.println("What is the path of the cafe_barista_men.bps file?");
            cafeBaristaPatchPath = System.console().readLine().trim().replaceAll("\"", "");
        }
        
        // If no patches are to be applied, exit
        if (!Men1Patch && !Men2Patch && !cafeBaristaPatch) {
        	log("No patches to apply!", LogLevel.FATAL);
        	System.exit(0);
        }

        // Fetch original ROMs
        System.out.println("What is the region of the Wii U console? (EU/US/JP)");
        String region = System.console().readLine().trim().toUpperCase();

        String regionCode = "";
        switch (region) {
            case "EU":
                regionCode = "10040200";
                break;
            case "US":
                regionCode = "10040100";
                break;
            case "JP":
                regionCode = "10040000";
                break;
            default:
                log("Invalid region. Exiting.", LogLevel.FATAL);
                return;
        }

        log("Fetching files from Wii U...", LogLevel.INFO);
        log("Using region code: " + regionCode, LogLevel.DEBUG);

        if (Men1Patch) {
            log("Fetching Men.pack file", LogLevel.INFO);
            FTP.getFile(ip, "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Package/Men.pack");
        }

        if (Men2Patch) {
            log("Fetching Men2.pack file", LogLevel.INFO);
            FTP.getFile(ip, "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Package/Men2.pack");
        }

        if (cafeBaristaPatch) {
            log("Fetching cafe_barista_men.bfsar file", LogLevel.INFO);
            FTP.getFile(ip, "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Sound/Men/cafe_barista_men.bfsar");
        }

        // Apply patches
        if (Men1Patch) {
            log("Initializing Men.pack patch", LogLevel.INFO);
            log(Men1PatchPath, LogLevel.DEBUG);
            String outputPath = themeName + "/content/Common/Package/Men.pack";
            String ftpPath = "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Package/Men.pack";
            if (Men1PatchPath.endsWith(".bps")) {
                try {
                    Patcher.BPSpatcher.applyPatch(FTP.getLocalPath(ftpPath), Men1PatchPath, outputPath);
                } catch (IOException e) {
                    log("Failed to apply patch: " + e.getMessage(), LogLevel.ERROR);
                    return;
                }
            } else {
                log("Only .bps patches are supported at this time.", LogLevel.FATAL);
                return;
            }
        }

        if (Men2Patch) {
            log("Initializing Men2.pack patch", LogLevel.INFO);
            log(Men2PatchPath, LogLevel.DEBUG);
            String outputPath = themeName + "/content/Common/Package/Men2.pack";
            String ftpPath = "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Package/Men2.pack";
            if (Men2PatchPath.endsWith(".bps")) {
                try {
                    Patcher.BPSpatcher.applyPatch(FTP.getLocalPath(ftpPath), Men2PatchPath, outputPath);
                } catch (IOException e) {
                    log("Failed to apply patch: " + e.getMessage(), LogLevel.ERROR);
                    return;
                }
            } else {
                log("Only .bps patches are supported at this time.", LogLevel.FATAL);
                return;
            }
        }

        if (cafeBaristaPatch) {
            log("Initializing cafe_barista_men.bfsar patch", LogLevel.INFO);
            log(cafeBaristaPatchPath, LogLevel.DEBUG);
            String outputPath = themeName + "/content/Common/Package/Sound/Men/cafe_barista_men.bfsar";
            String ftpPath = "storage_mlc/sys/title/00050010/" + regionCode + "/content/Common/Sound/Men/cafe_barista_men.bfsar";
            if (cafeBaristaPatchPath.endsWith(".bps")) {
                try {
                    Patcher.BPSpatcher.applyPatch(FTP.getLocalPath(ftpPath), cafeBaristaPatchPath, outputPath);
                } catch (IOException e) {
                    log("Failed to apply patch: " + e.getMessage(), LogLevel.ERROR);
                    return;
                }
            } else {
                log("Only .bps patches are supported at this time.", LogLevel.FATAL);
                return;
            }
        }
    }
}
