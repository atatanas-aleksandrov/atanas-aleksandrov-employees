package com.employees.employees.service.employee;

import com.employees.employees.domain.entity.Entry;
import com.employees.employees.domain.response.PairResponse;
import com.employees.employees.repository.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class EmployeeRecordsServiceImpl implements EmployeeRecordsService {

    private final EmployeeRepository repository;

    public EmployeeRecordsServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PairResponse> longestWorkingPairs(String fileName) {
        List<Entry> entries = repository.getEntries(fileName);

       return entries.stream().collect(Collectors.groupingBy(Entry::getProjectID))
               .entrySet().stream()
               .flatMap(projectEntries -> {
                   List<Entry> filteredEntries = projectEntries.getValue();
                   Integer projectID = projectEntries.getKey();

                   List<PairResponse> pairs = new ArrayList<>();

                   return combinations(filteredEntries).stream()
                           .map(pair -> {
                               Entry e1 = pair.get(0);
                               Entry e2 = pair.get(1);
                               long overlapDays = calculateOverlap(e1,e2);
                               return new PairResponse(e1.getEmpID(), e2.getEmpID(), projectID, overlapDays);
                           })
                           .filter(pair -> pair.daysWorked() > 0) // Only consider pairs with actual overlap
                           .max(Comparator.comparingLong(PairResponse::daysWorked)).stream(); // Find the pair with the longest overlap
               }).collect(Collectors.toList());
    }


    private static List<List<Entry>> combinations(List<Entry> entries) {
        return IntStream.range(0,entries.size())
                .boxed()
                .flatMap( i -> IntStream.range(i +1, entries.size())
                        .mapToObj(j -> Arrays.asList(entries.get(i), entries.get(j)))
                ).collect(Collectors.toList());
    }

    private static Long calculateOverlap(Entry entry1, Entry entry2){
        LocalDate overlapStart = Collections.max(Arrays.asList(entry1.getDateFrom(),entry2.getDateFrom()));
        LocalDate overlapEnd = Collections.min(Arrays.asList(entry1.getDateTo(),entry2.getDateTo()));

        return !overlapStart.isAfter(overlapEnd) ?
                Duration.between(overlapStart.atStartOfDay(), overlapEnd.plusDays(1)
                        .atStartOfDay()).toDays()
                : 0;

    }

}
