package dev.riley0122.wutt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import dev.riley0122.wutt.Main.LogLevel;

public class Patcher {
    public class BPSpatcher {
        private static int offset = 0;

        private static long decodeVarInt(byte[] patchdata) {
            long data = 0;
            int shift = 1;

            while (true) {
                byte x = patchdata[offset];
                offset += 1;
                data += (x & 0x7f) * shift;
                if ((x & 0x80) == 0x80) {
                    break;
                }
                shift <<= 7;
                data += shift;
            }

            return data;
        }

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

            byte[] orignalData;
            byte[] patchData;

            try {
                orignalData = Files.readAllBytes(original.toPath());
                patchData = Files.readAllBytes(patch.toPath());
            } catch (IOException e) {
                System.out.println("Failed to read files: " + e.getMessage());
                return;
            }

            if (patchData[0] != 'B' || patchData[1] != 'P' || patchData[2] != 'S' || patchData[3] != '1') {
                System.out.println("Invalid BPS patch file: " + patchFile);
                return;
            }
            offset = 4;

            // Actually apply the patch
            long sourceSize = decodeVarInt(patchData);
            long targetSize = decodeVarInt(patchData);
            long MetaDataSize = decodeVarInt(patchData);
            offset += MetaDataSize; // Skip metadata

            byte[] outputData = new byte[(int) targetSize + 1];
            int outputOffset = 0;

            int sourceRelativeOffset = 0;
            int targetrelativeOffset = 0;

            int sourceReads = 0;
            int targetReads = 0;
            int sourceCopies = 0;
            int targetCopies = 0;

            while (outputOffset < targetSize) {
                long data = decodeVarInt(patchData);
                long action = data & 0x3;
                long length = (data >> 2) + 1;

                if (action == 0) { // SourceRead
                    sourceReads += 1;
                    while (length > 0) {
                        length -= 1;
                        outputData[outputOffset] = orignalData[outputOffset];
                        outputOffset += 1;
                    }
                } else if (action == 1) { // TargetRead
                    targetReads += 1;
                    while (length > 0) {
                        length -= 1;
                        outputOffset += 1;
                        outputData[outputOffset] = patchData[offset];
                        offset += 1;
                    }
                } else if (action == 2) { // SourceCopy
                    sourceCopies += 1;
                    long data_m = decodeVarInt(patchData);
                    sourceRelativeOffset += ((data & 1) == 1 ? -1 : 1) * (data_m >> 1);
                    while (length > 0) {
                        length -= 1;
                        outputOffset += 1;
                        sourceRelativeOffset += 1;
                        outputData[outputOffset] = orignalData[sourceRelativeOffset];
                    }
                } else if (action == 3) { // TargetCopy
                    targetCopies += 1;
                    long data_mm = decodeVarInt(patchData);
                    targetrelativeOffset += ((data_mm & 1) == 1 ? -1 : 1) * (data_mm >> 1);
                    while (length > 0) {
                        length -= 1;
                        outputOffset += 1;
                        targetrelativeOffset += 1;
                        outputData[outputOffset] = outputData[targetrelativeOffset];
                    }
                } else {
                    Main.log("Invalid action in bps file!", LogLevel.FATAL);
                    return;
                }
            }

            Main.log("Finished applying BPS patch.", LogLevel.INFO);
            Main.log("Writing output file...", LogLevel.INFO);
            FileWriter outputWriter = new FileWriter(output, false);
            outputWriter.write(new String(orignalData, 0, (int) sourceSize));
        }
    }
}