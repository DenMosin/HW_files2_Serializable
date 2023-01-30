import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class Main {
    public static void main(String[] args) {

        GameProgress elf = new GameProgress(546, 1000, 43, 24.19);
        GameProgress ork = new GameProgress(583, 1122, 56, 45.77);
        GameProgress human = new GameProgress(865, 5464, 77, 10.10);

        saveGame("C:\\Games\\savegames\\sav.txt", elf);
        saveGame("C:\\Games\\savegames\\sav2.txt", ork);
        saveGame("C:\\Games\\savegames\\sav3.txt", human);

        zipFiles("C:\\Games\\savegames\\sav.zip", "C:\\Games\\savegames\\sav.txt",
                "C:\\Games\\savegames\\sav2.txt",
                "C:\\Games\\savegames\\sav3.txt");

        openZip("C:\\Games\\savegames\\sav.zip", "C:\\Games\\savegames");

        openProgress("C:\\Games\\savegames\\sav3.txt");

    }

    static void saveGame(String string, GameProgress gameProgress) {

        try {
            FileOutputStream fos = new FileOutputStream(string);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameProgress);
            fos.close();
            oos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static void zipFiles(String zip, String... files) {

        try {
            ZipOutputStream zot = new ZipOutputStream(new FileOutputStream(zip));

            try {
                for (String s : files) {
                    File fileZip = new File(s);
                    FileInputStream fis = new FileInputStream(fileZip);
                    {
                        ZipEntry entry = new ZipEntry(fileZip.getName());
                        zot.putNextEntry(entry);
                        // считываем содержимое файла в массив byte
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        // добавляем содержимое к архиву
                        zot.write(buffer);
                        fis.close();
                    }
                }
                // закрываем текущую запись для новой записи
                zot.close();

                for (String f : files) {
                    try {
                        Files.delete(Paths.get(f));
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String zipOpen, String pack) {

        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipOpen));
            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {
                File newFile = new File(pack, String.valueOf(entry));

                // распаковка
                FileOutputStream fot = new FileOutputStream(newFile);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fot.write(c);
                }
                fot.close();
                entry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Object openProgress(String save) {
        GameProgress gameProgress = null;
            // откроем входной поток для чтения файла
        try (FileInputStream fis = new FileInputStream(save); ObjectInputStream ois = new ObjectInputStream(fis)) {
            // десериализуем объект и скастим его в класс
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(gameProgress);
        return gameProgress;

    }
}






