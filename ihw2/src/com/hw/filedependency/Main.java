package com.hw.filedependency;

import java.io.IOException;

/**
 * Основной класс с единственным методом.
 */
public class Main {
    /**
     * Проверяется зацикленность, если её нет, сразу выводятся список и текст.
     * Если есть зацикленность, сначала выводятся файлы с ней. Список и текст не учитывают такие файлы.
     * При наличии IOException выводится отдельное сообщение, так как здесь это наиболее вероятное исключение.
     */
    public static void main(String[] args) {
        System.out.println("Checking Catalogue for files with dependencies...");
        try {
            CheckingCatalogue check = new CheckingCatalogue();
            FileContent content = new FileContent();
            if (content.filesAreLooped(check.files)) {
                check.loopedCheck(content.looped);
            }
            content.concatenation();
            check.normalCheck(content.allFiles, String.valueOf(content.text));
        } catch (IOException e) {
            System.out.println("It seems there are file access problems.");
        } catch (Exception e) {
            System.out.println("Something wrong happened interrupting the process.");
        }
    }
}
