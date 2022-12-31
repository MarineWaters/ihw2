package com.hw.filedependency;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Класс с основной работой с файлами.
 * Читает, проверяет на зацикленность, сортирует и компонует.
 */
public class FileContent {
    void readContent(List<Path> files) throws IOException {
        for (var file : files) {
            String name;
            List<String> req = new ArrayList<>();
            StringBuilder txt = new StringBuilder();
            String[] u = file.getParent().toString().split("\\\\");
            name = u[u.length - 1] + "/" + file.getFileName();
            try (FileReader reader = new FileReader(file.toFile()); Scanner scan = new Scanner(reader)) {
                while (scan.hasNextLine()) {
                    var line = scan.nextLine();
                    if (line.startsWith("require '")) {
                        req.add((line.split("'"))[1]);
                    }
                    txt.append(line).append("\n");
                }
            }
            allFiles.add(new FileInfo(name, txt.toString(), req));
        }
    }


    boolean filesAreLooped(List<Path> files) throws IOException {
        readContent(files);
        sortFiles();
        return looped.size() != 0;
    }

    void sortFiles() {
        Vector<FileInfo> sorted = new Vector<>();
        for (var file : allFiles) {
            if (!sorted.contains(file)) {
                int i = 0;
                int place = -1;
                while (i != sorted.size()) {
                    for (var req : file.requirements()) {
                        if (sorted.elementAt(i).nameAndFolder().contains(req) ||
                            req.contains(sorted.elementAt(i).nameAndFolder())) {
                            for (var r : sorted.elementAt(i).requirements()) {
                                if (r.contains(req) || req.contains(r)) {
                                    if (!looped.contains(r)) {
                                        looped.add(r);
                                    }
                                    if (!looped.contains(file.nameAndFolder())) {
                                        looped.add(file.nameAndFolder());
                                    }
                                }
                            }
                            place = i + 1;
                            break;
                        }
                    }
                    i++;
                }
                if (place != -1) {
                    sorted.insertElementAt(file, place);
                } else {
                    sorted.insertElementAt(file, sorted.size());
                }
            }
        }
        allFiles = sorted;
    }

    void concatenation() {
        for (var file : allFiles) {
            text.append(file.fullText());
        }
    }

    StringBuilder text = new StringBuilder();
    Vector<FileInfo> allFiles = new Vector<>();
    Vector<String> looped = new Vector<>();
}
