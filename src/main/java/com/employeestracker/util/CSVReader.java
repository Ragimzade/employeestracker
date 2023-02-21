package com.employeestracker.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CSVReader<T> {

    public List<T> read(MultipartFile file, Class<T> type) {
        CsvToBean<T> csvToBean;
        try {
            csvToBean = new CsvToBeanBuilder<T>(new InputStreamReader(file.getInputStream()))
                    .withType(type)
                    .build();
            return new ArrayList<>(csvToBean.parse());
        } catch (IOException e) {
            log.error("Unable to read file at {}, {}", file, e);
            throw new RuntimeException(e.getMessage());
        }
    }
}