package com.employees.employees;

import com.employees.employees.domain.exception.storage.StorageFileNotFoundException;
import com.employees.employees.repository.file.FileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class FileRepositoryTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileRepository fileRepository;

    @Test
    public void shouldListAllFiles() throws Exception {
     given(this.fileRepository.loadAll()).willReturn(
             Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

     this.mvc.perform(get("/files"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$[0]").value("http://localhost/files/first.txt"))
             .andExpect(jsonPath("$[1]").value("http://localhost/files/second.txt"));
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        this.mvc.perform(multipart("/files").file(multipartFile))
                .andExpect(status().isCreated());

        then(this.fileRepository).should().store(multipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should404WhenMissingFile() throws Exception {
        given(this.fileRepository.loadAsResource("test.txt"))
                .willThrow(StorageFileNotFoundException.class);

        this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
    }

}
