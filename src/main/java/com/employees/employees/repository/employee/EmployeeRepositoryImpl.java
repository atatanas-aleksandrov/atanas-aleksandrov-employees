package com.employees.employees.repository.employee;

import com.employees.employees.config.FileStorageConfig;
import com.employees.employees.domain.entity.Entry;
import com.employees.employees.domain.exception.storage.StorageException;
import com.employees.employees.utility.DateUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final Path rootLocation;

    public EmployeeRepositoryImpl(FileStorageConfig fileStorageConfig) {
        this.rootLocation = Paths.get(fileStorageConfig.getLocation());
    }

    @Override
    public List<Entry> getEntries(String fileName) {
        List<Entry> entries = new ArrayList<>();

        try(CSVReader reader =
                    new CSVReader(Files.newBufferedReader(rootLocation.resolve(fileName)))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                entries.add(new Entry(
                        Integer.parseInt(line[0]),
                        Integer.parseInt(line[1]),
                        DateUtil.parseDate(line[2]),
                        DateUtil.parseDate(line[3])
                ));
            }
        } catch (CsvValidationException | IOException e) {
            throw new StorageException("There was a problem loading the file",e);
        }

        return entries;
    }
}
