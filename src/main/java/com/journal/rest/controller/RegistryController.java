package com.journal.rest.controller;


import com.journal.data.dto.*;
import com.journal.rest.BaseResponse;
import com.journal.service.RegistryReportBuilder;
import com.journal.service.RegistryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/registry")
@RequiredArgsConstructor
public class RegistryController {

    private final RegistryService registryService;
    private final RegistryReportBuilder registryReportBuilder;

    @PostMapping
    @ApiOperation("add record to registry")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add record"),
            @ApiResponse(code = 401, message = "You are not authenticated to view this resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PreAuthorize("hasAuthority('MONITOR')")
    public void addRecord(@RequestBody @Valid AddRecordDto dto) {
        registryService.addRecord(dto);
    }

    @GetMapping
    @ApiOperation("get registry by date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully add record"),
            @ApiResponse(code = 401, message = "You are not authenticated to view this resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT', 'MONITOR')")
    public BaseResponse<RegistryResponseDto> getRegistryForDate(@RequestParam(required = false) String date,
                                                                @RequestParam(required = false, defaultValue = "ua") String lang) {
        return new BaseResponse<>(registryService.findAllByDate(date, lang));
    }

    @PostMapping(value = "/report", produces = "application/vnd.ms-excel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get report"),
            @ApiResponse(code = 401, message = "You are not authenticated to view this resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MONITOR')")
    public ResponseEntity<InputStreamResource> getRegistryReport(@RequestBody @Valid RegistryByDatesDto request,
    @RequestParam(required = false, defaultValue = "ua") String lang) {

        LocalDate dateFrom = request.getDateFrom();
        LocalDate dateTo = request.getDateTo();

        ByteArrayInputStream report = registryReportBuilder.export(dateFrom, dateTo, lang);

        String fileName = dateFrom.equals(dateTo) ? "Report_" + dateFrom +".xls"
                : "Report_" + dateFrom + "_to_" + dateTo + ".xls";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(new InputStreamResource(report));
    }

    @PostMapping("/user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get registry by user id and dates interval"),
            @ApiResponse(code = 401, message = "You are not authenticated to view this resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT', 'MONITOR')")
    public BaseResponse<List<RegistryByUserAndDatesDtoResponse>> getRegistryByUserAndDates(
            @RequestBody @Valid RegistryByUserAndDatesDtoRequest request,
            @RequestParam(required = false, defaultValue = "ua") String lang){
        return new BaseResponse<>(registryService.getRegistryByUserIdAndDates(request, lang));
    }

}