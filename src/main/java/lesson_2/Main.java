package lesson_2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {

    public static void main(String[] args) throws IOException {
//        Path path = Paths.get("/home..."); old method
//        путь может быть абсолютный и относительный
        Path path = Path.of("src/main/java/lesson_2/Main.java");
        Path path2 = Path.of("./tmp/file"); // путь относительно директории где запущена программа

        try {
            Files.createDirectories(Path.of("dir"));
        } catch (FileAlreadyExistsException e){
            // бросить рантайм или написать сообщение что файл уже существует
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        Files.copy(path, path2, StandardCopyOption.REPLACE_EXISTING);
        // есть методы для перемещения, переименовывания и удаления файлов

        // обход директории, в переопределяемых методах реализуем действия при определенных событиях
        Files.walkFileTree(Path.of("dir"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (dir.endsWith("d")) {
                    return FileVisitResult.CONTINUE;
                    // когда дошли до дир но не вошли в нее можем например пропустить ее если она заканчивается на символ "d"
                } else {
                    return FileVisitResult.TERMINATE;
                }
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.TERMINATE; // stop
//                return null;
                // когда уже указатель на файле можем либо считать его, либо удалить
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return null;
                //  действие при ошибке (файл недоступен, не хватает прав на чтение, файл занят другой программой)
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return null;
                // действие когда мы вышли из директории, можем например ее удалить если в ней удалили все файлы
            }
        });

        // размер файла ограничен int это 2 с лишним гб, если файл больше в массив все не влезет,
        // по этому перед считыванием надо проверять размер файла
        // так же байтовый массив будет располагаться в хипе, по этому при чтении нескольких файлов его может не хватить
        byte[] bytes = Files.readAllBytes(Path.of("file.txt"));
        Files.size(Path.of("file.txt")); // look file size
        // java -XX:+PrintFlagsFinal -version | grep HeapSize // look heap size

//        RandomAccessFile позволяет вставать на любую позицию в файле и вычитывать по частям

//        FileChannel позволяет вычитывать в буфер как из стрима

        FileChannel fileChannel = FileChannel.open(Path.of("file.txt"));
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 10);// 10 mb
        fileChannel.read(buffer); // будет вычитывать из файла и последовательно передавать в буфер
    }
}
