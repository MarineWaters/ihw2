package com.hw.filedependency;

import java.util.List;

/**
 * @param nameAndFolder имя файла и папки над ним
 * @param fullText текст в файле
 * @param requirements требования к другим файлам
 * Класс для сохранения вышеуказанных параметров для каждого файла.
 */
public record FileInfo(String nameAndFolder, String fullText, List<String> requirements) {
}