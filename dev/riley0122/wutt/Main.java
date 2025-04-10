package dev.riley0122.wutt;

public class Main {
    public static boolean Men1Patch = false;
    public static String Men1PatchPath = "";
    public static boolean Men2Patch = false;
    public static String Men2PatchPath = "";
    public static boolean cafeBaristaPatch = false;
    public static String cafeBaristaPatchPath = "";

    public static String ip = "";

    public static void main(String[] args) {
        System.out.println("Wii U Theme Tool (WUTT) - v1.0.0");
        System.out.println("By Riley0122 (riley0122.dev)");

        System.out.println("What is the IP address of you Wii U console?");
        ip = System.console().readLine().trim();
        if (ip.isBlank()) {
            System.out.println("IP address cannot be empty. Exiting.");
            return;
        }

        System.out.println("Is there a Men.bps file you want to apply? (y/N)");
        String input = System.console().readLine().trim().toLowerCase();
        Men1Patch = input.equals("y") || input.equals("yes");

        if (Men1Patch) {
            System.out.println("What is the path of the Men.bps file?");
            Men1PatchPath = System.console().readLine().trim();
        }

        System.out.println("Is there a Men2.bps file you want to apply? (y/N)");
        input = System.console().readLine().trim().toLowerCase();
        Men2Patch = input.equals("y") || input.equals("yes");

        if (Men2Patch) {
            System.out.println("What is the path of the Men2.bps file?");
            Men2PatchPath = System.console().readLine().trim();
        }

        System.out.println("Is there a cafe_barista_men.bps file you want to apply? (y/N)");
        input = System.console().readLine().trim().toLowerCase();
        cafeBaristaPatch = input.equals("y") || input.equals("yes");

        if (cafeBaristaPatch) {
            System.out.println("What is the path of the cafe_barista_men.bps file?");
        }
    }
}
