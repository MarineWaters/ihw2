package com.hw.filedependency;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Класс, при создании собирающий список всех файлов в подпапках и отдельно сохраняющий этот список.
 * Также имеет методы, вызываемые в зависимости от наличия зацикленности файлов.
 */
public class CheckingCatalogue {
    CheckingCatalogue() throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get("./src/Catalogue"))) {
            files = stream.filter(Files::isRegularFile).toList();
        }
    }

    void loopedCheck(Vector<String> loopedFiles) {
        System.out.println("Unfortunately, there are several files creating a loop. Such files are:");
        for (var file : loopedFiles) {
            System.out.println(file);
        }
    }

    void normalCheck(Vector<FileInfo> sortedFiles, String newText) throws IOException {
        System.out.println("File sorting was successful. The correct order is:");
        for (var file : sortedFiles) {
            System.out.println(file.nameAndFolder());
        }
        System.out.println("New text created with concatenation is:");
        System.out.println(newText);
        try (FileWriter writer = new FileWriter("new.txt")) {
            writer.write(newText);
        }
    }

    List<Path> files;
}
