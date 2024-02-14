package app.estateagency.services;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipperService {
    public ByteArrayOutputStream zipFolder(String folderName) throws IOException {
        File folder = new File(folderName);

        if (!folder.isDirectory())
            throw new IllegalArgumentException("Provided file is not a directory");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try (ZipOutputStream zipStream = new ZipOutputStream(stream)) {
            addFolderToZip(folder, folder.getName(), zipStream);
        }

        return stream;
    }

    private void addFolderToZip(File folder, String folderName, ZipOutputStream zipStream) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) addFolderToZip(file, folderName + "/" + file.getName(), zipStream);
            else addFileToZip(file, folderName + "/" + file.getName(), zipStream);
        }
    }

    private void addFileToZip(File file, String fileName, ZipOutputStream zipStream) {
        byte[] buffer = new byte[1024];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            zipStream.putNextEntry(new ZipEntry(fileName));
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipStream.write(buffer, 0, length);
            }
            zipStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
