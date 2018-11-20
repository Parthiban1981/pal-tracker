package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TimeEntryController {
    TimeEntryRepository repository;
    List<TimeEntry> entries = new ArrayList<>();

    public TimeEntryController(TimeEntryRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity

    create(@RequestBody TimeEntry timeEntry) {
        return new ResponseEntity(repository.create(timeEntry), HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry entry = repository.find(id);
        if (entry != null) {
            return new ResponseEntity(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity(entry, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry entry = repository.update(id, timeEntry);
        if (entry != null) {
            return new ResponseEntity(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity(entry, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<List<TimeEntry>>(repository.list(), HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        return new ResponseEntity(repository.delete(id), HttpStatus.NO_CONTENT);
    }

}
