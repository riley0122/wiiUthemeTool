package dev.riley0122.wutt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Patcher {
    public class BPSpatcher {
        public static void applyPatch(String originalRom, String patchFile, String outputRom) {
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
                    output.createNewFile();
                } catch (IOException e) {
                    System.out.println("Failed to create output file: " + outputRom + " - " + e.getMessage());
                    return;
                }
            }

            byte[] orignalData;
            int orignalDataPointer = 0;
            byte[] patchData;
            int patchDataPointer = 0;

            try {
                orignalData = Files.readAllBytes(original.toPath());
                patchData = Files.readAllBytes(patch.toPath());
            } catch (IOException e) {
                System.out.println("Failed to read files: " + e.getMessage());
                return;
            }

            if (patchData[0] != 'B' || patchData[1] != 'P' || patchData[2] != 'S' || patchData[3] != '1') {
                System.out.println("Invalid BPS patch file: " + patchFile);
                orignalDataPointer = 4;
                return;
            }

            // Actually apply the patch
            throw new UnsupportedOperationException("BPS patching not implemented yet.");
        }
    }
}