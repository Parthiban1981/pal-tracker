package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntriesRepo;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    //TimeEntryRepository repository;

/*
    public TimeEntryController(TimeEntryRepository repository) {
        this.repository = repository;
    }
    */


    public TimeEntryController(TimeEntryRepository timeEntriesRepo, MeterRegistry meterRegistry) {
        this.timeEntriesRepo = timeEntriesRepo;
        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }


    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = timeEntriesRepo.create(timeEntry);
        actionCounter.increment();
        timeEntrySummary.record(timeEntriesRepo.list().size());
        //return new ResponseEntity(repository.create(timeEntry), HttpStatus.CREATED);
        return new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        //TimeEntry entry = repository.find(id);
        TimeEntry entry = timeEntriesRepo.find(id);
        if (entry != null) {
            actionCounter.increment();
            return new ResponseEntity<>(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(entry, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry timeEntry) {
        //TimeEntry entry = repository.update(id, timeEntry);
        TimeEntry entry = timeEntriesRepo.update(id, timeEntry);
        if (entry != null) {
            actionCounter.increment();
            return new ResponseEntity<>(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(entry, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        //return new ResponseEntity<List<TimeEntry>>(repository.list(), HttpStatus.OK);
        return new ResponseEntity<List<TimeEntry>>(timeEntriesRepo.list(), HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
       // repository.delete(id);
        timeEntriesRepo.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(timeEntriesRepo.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
