package ru.tkachenko.springbooking.service.impl;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.dto.RoomBookedEvent;
import ru.tkachenko.springbooking.dto.UserRegisteredEvent;
import ru.tkachenko.springbooking.exception.CreateFolderException;
import ru.tkachenko.springbooking.service.StatisticService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseStatisticService implements StatisticService {
    private final MongoTemplate mongoTemplate;

    public void exportDataToCsv(String pathToFolder) {
        pathToFolder = pathToFolder.replace("/", File.separator);
        String path = pathToFolder + File.separator;
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new CreateFolderException("Не удалось создать каталог: " + directory.getAbsolutePath());
            }
        }
        exportRoomBookedEventDataToCsv(path);
        exportUserRegisteredEventDataToCsv(path);
    }

    private void exportRoomBookedEventDataToCsv(String path) {
        List<RoomBookedEvent> roomBookedEvents = mongoTemplate.findAll(RoomBookedEvent.class);
        try (CSVWriter writer = new CSVWriter(new FileWriter(path + "exportedRoomBookedEventData.csv"), ';', '"', '"', "\n")) {
            String[] header = {"userId", "checkInDate", "checkOutDate"};
            writer.writeNext(header);
            for (RoomBookedEvent event : roomBookedEvents) {
                String[] data = {String.valueOf(event.getUserId()), event.getCheckInDate(), event.getCheckOutDate()};
                writer.writeNext(data);
            }
        } catch (IOException e) {
            log.error("Ошибка при сохранении данных в CSV", e);
        }
    }

    private void exportUserRegisteredEventDataToCsv(String path) {
        List<UserRegisteredEvent> userRegisteredEvents = mongoTemplate.findAll(UserRegisteredEvent.class);
        try (CSVWriter writer = new CSVWriter(new FileWriter(path + "exportedUserRegisteredEventData.csv"), ';', '"', '"', "\n")) {
            String[] header = {"userId"};
            writer.writeNext(header);
            for (UserRegisteredEvent event : userRegisteredEvents) {
                String[] data = {String.valueOf(event.getUserId())};
                writer.writeNext(data);
            }
        } catch (IOException e) {
            log.error("Ошибка при сохранении данных в CSV", e);
        }
    }
}
