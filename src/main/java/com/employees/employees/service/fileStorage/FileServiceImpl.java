package com.employees.employees.service.fileStorage;

import com.employees.employees.config.FileStorageConfig;
import com.employees.employees.repository.file.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {


    private final FileRepository fileRepository;

    public FileServiceImpl(FileStorageConfig properties, FileRepository fileRepository) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("File location cannot be empty");
        }

        this.fileRepository = fileRepository;
    }

    @Override
    public void store(MultipartFile file) {
        this.fileRepository.store(file);
    }

    @Override
    public Stream<Path> loadAll() {
        return this.fileRepository.loadAll();
    }

    @Override
    public Path load(String filename) {
        return this.fileRepository.load(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
       return this.fileRepository.loadAsResource(filename);
    }

    @Override
    public void delete(String filename) {
        this.fileRepository.delete(filename);
    }
}
