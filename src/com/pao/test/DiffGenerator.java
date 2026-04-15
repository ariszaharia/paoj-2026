package com.pao.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class IncorrectExtensionException extends Exception {
    public IncorrectExtensionException(String expectedExtension, String filename) {
        super("Filename must end with '" + expectedExtension + "' but got: " + filename);
    }
}

public class DiffGenerator {
    public static void saveDiff(String expected, String actual, String filename) throws IncorrectExtensionException {
        if (!filename.endsWith(".diff")) {
            throw new IncorrectExtensionException(".diff", filename);
        }

        List<String> expectedOutput = Arrays.asList(expected.split("\\R", -1));
        List<String> actualOutput = Arrays.asList(actual.split("\\R", -1));
        List<String> unifiedDiff = buildSimpleDiff(expectedOutput, actualOutput);

        try {
            Path outputPath = Paths.get(filename);
            Files.write(outputPath, unifiedDiff, StandardCharsets.UTF_8);
            System.out.println("Diff file generated successfully at: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write diff file: " + e.getMessage());
        }
    }

    private static List<String> buildSimpleDiff(List<String> expectedLines, List<String> actualLines) {
        List<String> diff = new ArrayList<String>();
        diff.add("--- expected_output.txt");
        diff.add("+++ actual_output.txt");

        int max = Math.max(expectedLines.size(), actualLines.size());
        for (int i = 0; i < max; i++) {
            String expected = i < expectedLines.size() ? expectedLines.get(i) : null;
            String actual = i < actualLines.size() ? actualLines.get(i) : null;

            if (Objects.equals(expected, actual)) {
                continue;
            }

            diff.add("@@ line " + (i + 1) + " @@");
            if (expected != null) {
                diff.add("-" + expected);
            }
            if (actual != null) {
                diff.add("+" + actual);
            }
        }

        if (diff.size() == 2) {
            diff.add("(no textual differences)");
        }

        return diff;
    }
}