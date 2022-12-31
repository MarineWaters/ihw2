package com.hw.filedependency;

import java.io.IOException;

/**
 * Основной класс с единственным методом. Проверяется зацикленность, если её нет, выводится текст и список.
 * При наличии IOException выводится отдельное сообщение, так как здесь это наиболее вероятное исключение.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Checking Catalogue for files with dependencies...");
        try {
            CheckingCatalogue check = new CheckingCatalogue();
            FileContent content = new FileContent();
            if (content.filesAreLooped(check.files)) {
                check.loopedCheck(content.looped);
            } else {
                content.concatenation();
                check.normalCheck(content.allFiles, String.valueOf(content.text));
            }
        } catch (IOException e) {
            System.out.println("It seems there are file access problems.");
        } catch (Exception e) {
            System.out.println("Something wrong happened interrupting the process.");
        }
    }
}