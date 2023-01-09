package com.hw.filedependency;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Класс, при создании собирающий список всех файлов в подпапках и отдельно сохраняющий этот список.
 * Также имеет методы, вызываемые в зависимости от наличия зацикленности файлов.
 */
class CheckingCatalogue {
    /**
     * Конструктор, в котором должен быть получен список путей.
     *
     * @throws IOException конструктор может бросить исключение из-за некорректной работы Files.walk
     */
    CheckingCatalogue() throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get("./src/Catalogue"))) {
            files = stream.filter(Files::isRegularFile).toList();
        }
    }

    /**
     * Метод, используемый при наличии зацикленных файлов. Выводит их список.
     *
     * @param loopedFiles список зацикленных файлов
     */
    void loopedCheck(Vector<String> loopedFiles) {
        System.out.println("\nUnfortunately, there are several files creating a loop. Such files are:");
        for (var file : loopedFiles) {
            System.out.println(file);
        }
    }

    /**
     * Метод, работающий с незацикленным списком файлов. Выводит список, полученный текст, а также вписывает текст
     * в отдельный файл.
     *
     * @param sortedFiles отсортированный список незацикленных файлов
     * @param newText     соединённый текст файлов из отсортированного списка
     * @throws IOException если new.txt недоступен или не может быть создан, может быть брошено исключение
     */
    void normalCheck(Vector<FileInfo> sortedFiles, String newText) throws IOException {
        System.out.println("\nFile sorting was successful. The correct order is:");
        for (var file : sortedFiles) {
            System.out.println(file.nameAndFolder());
        }
        System.out.println("\nNew text created with concatenation is:");
        System.out.println(newText);
        try (FileWriter writer = new FileWriter("new.txt")) {
            writer.write(newText);
        }
    }

    /**
     * Список путей.
     */
    List<Path> files;
}
