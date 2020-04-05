package com.mohit.coronaVirusdashBoard.controllers;

import com.mohit.coronaVirusdashBoard.constants.AppConstants;
import com.mohit.coronaVirusdashBoard.models.LocationStats;
import com.mohit.coronaVirusdashBoard.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    private static List<LocationStats> allLocationStats;

    @GetMapping("/")
    public String home(Model model) throws IOException, InterruptedException {

        allLocationStats = coronaVirusDataService.fetchData(AppConstants.CONFIRMED_CASES_URL);
        int totalCasesReported = allLocationStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int newCasesReported = allLocationStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("locationStats", allLocationStats);
        model.addAttribute("totalReportedCases", totalCasesReported);
        model.addAttribute("newCasesReported", newCasesReported);
        return "home";

    }

    @PostMapping("/findByCountry")
    public String findByCountry(@RequestParam String country, Model model) throws IOException, InterruptedException {

        allLocationStats = coronaVirusDataService.fetchData(AppConstants.CONFIRMED_CASES_URL);
        Map<String, List<LocationStats>> stateCountryLatestCases = allLocationStats.stream().collect(
                Collectors.groupingBy(
                        LocationStats::getCountry, Collectors.toList())
        );
        List<LocationStats> locationStatsByCountry = stateCountryLatestCases.entrySet()
                .stream()
                .filter(stat -> stat.getKey().equalsIgnoreCase(country))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        model.addAttribute("locationStats", locationStatsByCountry);
        int totalCasesInCountry = locationStatsByCountry.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int newCasesReported = locationStatsByCountry.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("totalCasesInCountry",totalCasesInCountry);
        model.addAttribute("newCasesReported",newCasesReported);
        return "country";
    }

    @GetMapping("/getDeathData")
    public String getDeathDetails(Model model) throws IOException, InterruptedException {

        allLocationStats = coronaVirusDataService.fetchData(AppConstants.DEATH_DATA_URL);
        int totalCasesReported = allLocationStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int newCasesreported = allLocationStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("locationStats", allLocationStats);
        model.addAttribute("totalReportedCases", totalCasesReported);
        model.addAttribute("newCasesReported", newCasesreported);
        return "deathdatadetails";

    }

}
