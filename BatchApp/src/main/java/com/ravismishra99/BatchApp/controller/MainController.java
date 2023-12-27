package com.ravismishra99.BatchApp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ravismishra99.BatchApp.entity.Product;
import com.ravismishra99.BatchApp.generator.ExcelGenerator;
import com.ravismishra99.BatchApp.repository.ProductRepository;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/product")
public class MainController {

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;
	@Autowired
	private ProductRepository repository;

	@PostMapping("/import")
	public void importProduct() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
		
	@GetMapping("/excel/product.csv")
	public void exportAsExcel(HttpServletResponse response) throws IOException
	{
		response.setContentType("application/octet-stream");
		List<Product> productList = repository.findAll();
		ExcelGenerator generator = new ExcelGenerator(productList);
		generator.generateExcelFile(response);
	}

}
