package com.employees.employees.repository.file;

import com.employees.employees.config.FileStorageConfig;
import com.employees.employees.domain.exception.storage.FileFormatException;
import com.employees.employees.domain.exception.storage.StorageException;
import com.employees.employees.domain.exception.storage.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Repository
public class FileRepositoryImpl implements FileRepository {

    private final Path rootLocation;

    public FileRepositoryImpl(FileStorageConfig properties) {

        if (properties.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("File location cannot be empty");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Unable to create directory", e);
        }
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()){
                throw new StorageException("Failed to store empty file");
            }

            if (!file.getOriginalFilename().endsWith(".csv")){
                throw new FileFormatException("Please provide the file in csv format.");
            }

            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("File can not be stored outside of the current directory");
            }

            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }   catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation,1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }catch (IOException e) {
            throw new StorageException("Failed to load files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return this.rootLocation.resolve(filename);
    }

    @Override
    public void delete(String filename) {
        try {
            Files.delete(this.rootLocation.resolve(filename));
        }catch (IOException e) {
            throw new StorageException("Failed to delete file", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }

        }catch (MalformedURLException e){
            throw new StorageFileNotFoundException("Failed to load file", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
    }
}
