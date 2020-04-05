package com.mohit.coronaVirusdashBoard.services;


import com.mohit.coronaVirusdashBoard.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CoronaVirusDataService {

   // @Scheduled(cron = "* * 1 * * *")
    public List<LocationStats> fetchData(String url) throws IOException, InterruptedException {

        List<LocationStats> newStats = new ArrayList<>();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStats = new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDay = Integer.parseInt(record.get(record.size() - 2));
            int diffFromPrevDay = Math.abs(latestCases - prevDay);
            locationStats.setLatestTotalCases(latestCases);
            locationStats.setDiffFromPreviousDay(diffFromPrevDay);
            newStats.add(locationStats);
        }

        List<LocationStats> resultData = newStats.stream()
                .sorted(Comparator.comparing(LocationStats::getDiffFromPreviousDay)
                        .reversed()).collect(Collectors.toList());
        return resultData;
    }
}
