package com.employees.employees.controller;

import com.employees.employees.domain.exception.storage.StorageFileNotFoundException;
import com.employees.employees.service.fileStorage.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<String> listAllFiles(){
      return fileService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(
                        FileController.class,
                        "serveFile",
                        path.getFileName().toString()).toUriString()
        ).collect(Collectors.toList());
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = fileService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void handleFileUpload(@RequestParam("file") MultipartFile file) {
        fileService.store(file);
    }

    @DeleteMapping("/{filename:.+}")
    public void delete(@PathVariable String filename) {
        this.fileService.delete(filename);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
