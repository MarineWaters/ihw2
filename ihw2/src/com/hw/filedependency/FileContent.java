package com.hw.filedependency;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Класс с основной работой с файлами.
 * Читает, проверяет на зацикленность, сортирует и компонует.
 */
class FileContent {
    /**
     * Метод, считывающий и сохраняющий имя с папкой, текст и требования(через налличие require) файлов.
     *
     * @param files список файлов в каталоге
     * @throws IOException если файл из списка недоступен, может быть брошено исключение
     */
    private void readContent(List<Path> files) throws IOException {
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


    /**
     * Метод, возвращающий факт наличия зацикленных файлов посредством проверки размера списка,
     * который может стать ненулевым при сортировке в sortFiles.
     * Также изначально переходит к чтению файлов, чтобы приступить к сортировке после этого.
     *
     * @param files полный список файлов
     * @return возвращает true, если есть зацикленные файлы, иначе false
     * @throws IOException используется метод readContent, который может бросить исключение
     */
    boolean filesAreLooped(List<Path> files) throws IOException {
        readContent(files);
        sortFiles();
        return looped.size() != 0;
    }

    /**
     * Сортировка в 3 этапа: сначала без учёта циклов происходит перемещение файлов в зависимости от их требований,
     * после этого находятся все зацикленные файлы, затем они удаляются из общего отсортированного списка.
     */
    private void sortFiles() {
        Vector<FileInfo> sorted = new Vector<>();
        for (var file : allFiles) {
            if (!sorted.contains(file)) {
                int i = 0;
                int place = 0;
                while (i != sorted.size()) {
                    if (file.requirements().contains(sorted.elementAt(i).nameAndFolder())) {
                        place = i + 1;
                    }
                    i++;
                }
                sorted.insertElementAt(file, place);
            }
        }
        for (var i : sorted) {
            if (!looped.contains(i.nameAndFolder())) {
                looped.addAll(recursiveCheck(i.nameAndFolder(), i.nameAndFolder(), i.requirements()));
            }
        }
        sorted.removeIf(j -> looped.contains(j.nameAndFolder()));
        allFiles = sorted;
    }

    /**
     * Рекурсивный поиск зацикленных файлов. Если такой файл находится, все файлы на пути добавляются к вектору.
     * Иначе возвращается пустой вектор.
     * @param nameToFind файл, который ищется среди требований его собственных требований
     * @param currentFile имя текущего проверяемого файла
     * @param reqList список требований текущего файла
     * @return вектор, содержащий зацикленные файлы, имеющие в требованиях искомый(nameToFind)
     */
    private Vector<String> recursiveCheck(String nameToFind, String currentFile, List<String> reqList) {
        Vector<String> temporaryVector = new Vector<>();
        if (reqList.contains(nameToFind)) {
            if (!looped.contains(currentFile)) {
                temporaryVector.add(currentFile);
            }
        } else {
            for (var req : reqList) {
                if (!looped.contains(req)) {
                    for (var file : allFiles) {
                        if (Objects.equals(file.nameAndFolder(), req)
                            && temporaryVector.addAll(recursiveCheck(nameToFind, req, file.requirements()))
                            && !looped.contains(currentFile)) {
                            temporaryVector.add(currentFile);
                        }
                    }
                }
            }
        }
        return temporaryVector;
    }

    /**
     * Объединение текста всех незацикленных файлов в правильном порядке.
     */
    void concatenation() {
        for (var file : allFiles) {
            text.append(file.fullText());
        }
    }

    /**
     * Новый полный текст в правильном порядке и из корректных файлов.
     */
    StringBuilder text = new StringBuilder();
    /**
     * Список полученных файлов, затем выделяющий looped и оставляющий незацикленные файлы.
     */
    Vector<FileInfo> allFiles = new Vector<>();
    /**
     * Список зацикленных файлов.
     */
    Vector<String> looped = new Vector<>();
}
