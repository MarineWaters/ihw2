package com.hw.filedependency;

import java.util.List;

/**
 * Класс для сохранения указанных параметров для каждого файла.
 *
 * @param nameAndFolder имя файла и папки над ним
 * @param fullText      текст в файле
 * @param requirements  требования к другим файлам
 */
record FileInfo(String nameAndFolder, String fullText, List<String> requirements) {
}
