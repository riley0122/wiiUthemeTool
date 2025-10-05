package dev.riley0122.wutt;

import java.io.File;
import java.io.IOException;

public class Patcher {
    public class BPSpatcher {
        public static void applyPatch(String originalRom, String patchFile, String outputRom) throws IOException {
            System.out.println("Attempting to apply BPS patch... (" + originalRom + " + " + patchFile + " -> " + outputRom + ")");
            
            File original = new File(originalRom);
            File patch = new File(patchFile);
            File output = new File(outputRom);

            if (!original.exists()) {
                System.out.println("Original ROM file does not exist: " + originalRom);
                return;
            }

            if (!patch.exists()) {
                System.out.println("Patch file does not exist: " + patchFile);
                return;
            }

            if (output.exists()) {
                System.out.println("Output ROM file already exists: " + outputRom);
                return;
            } else {
                try {
                    output.getParentFile().mkdirs();
                    output.createNewFile();
                } catch (IOException e) {
                    System.out.println("Failed to create output file: " + outputRom + " - " + e.getMessage());
                    return;
                }
            }
            
            // TODO: apply the patch
        }
    }
}