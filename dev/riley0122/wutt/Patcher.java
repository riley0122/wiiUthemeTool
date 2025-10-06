package dev.riley0122.wutt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import dev.riley0122.wutt.Main.LogLevel;

public class Patcher {
    public class BPSpatcher {
        public static void applyPatch(String originalRom, String patchFile, String outputRom) throws IOException {
            Main.log("Attempting to apply BPS patch... (" + originalRom + " + " + patchFile + " -> " + outputRom + ")", LogLevel.INFO);
            
            File original = new File(originalRom);
            File patch_ = new File(patchFile);
            File output = new File(outputRom);

            if (!original.exists()) {
            	Main.log("Original ROM file does not exist: " + originalRom, Main.LogLevel.FATAL);
                return;
            }

            if (!patch_.exists()) {
            	Main.log("Patch file does not exist: " + patchFile, Main.LogLevel.FATAL);
                return;
            }

            if (!Main.allowOverwrite && output.exists()) {
            	Main.log("Output ROM file already exists: " + outputRom, Main.LogLevel.FATAL);
                return;
            } else {
                try {
                    output.getParentFile().mkdirs();
                    output.createNewFile();
                } catch (IOException e) {
                	Main.log("Failed to create output file: " + outputRom + " - " + e.getMessage(), Main.LogLevel.FATAL);
                    return;
                }
            }
            
            // Apply the patch
            byte[] source = Files.readAllBytes(original.toPath());
            byte[] patch_bytes = Files.readAllBytes(patch_.toPath());
            
            Patch patch = new Patch(patch_bytes);
        }
    }
}