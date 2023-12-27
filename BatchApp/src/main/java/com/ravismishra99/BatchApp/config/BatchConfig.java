package com.ravismishra99.BatchApp.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.ravismishra99.BatchApp.entity.Product;
import com.ravismishra99.BatchApp.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

	private final ProductRepository repository;

	@Bean
	public FlatFileItemReader<Product> read() {
		FlatFileItemReader<Product> fir = new FlatFileItemReader<Product>();
		fir.setResource(new FileSystemResource("src/main/resources/product.csv"));
		fir.setName("Product_Record");
		fir.setLinesToSkip(1);
		fir.setLineMapper(lineMapper());
		return fir;

	}

	public LineMapper<Product> lineMapper() {
		DefaultLineMapper<Product> dlm = new DefaultLineMapper<Product>();

		DelimitedLineTokenizer dt = new DelimitedLineTokenizer();
		dt.setDelimiter(",");
		dt.setStrict(false);
		dt.setNames("id", "title", "description", "image1", "image2","image3","createdAt","updatedAt","category_id","category_name","category_image","category_createdAt","category_updatedAt");

		BeanWrapperFieldSetMapper<Product> bfs = new BeanWrapperFieldSetMapper<Product>();
		bfs.setTargetType(Product.class);

		dlm.setLineTokenizer(dt);
		dlm.setFieldSetMapper(bfs);

		return dlm;

	}

	@Bean
	public ProductProcessor processor() {
		return new ProductProcessor();
	}

	@Bean
	public RepositoryItemWriter<Product> write() {
		RepositoryItemWriter<Product> rit = new RepositoryItemWriter<Product>();
		rit.setRepository(repository);
		rit.setMethodName("save");
		return rit;
	}

	@Bean
	public Job job(Step step, JobRepository jobRepository) {
		return new JobBuilder("myJob", jobRepository).incrementer(new RunIdIncrementer()).flow(step).end().build();
	}

	@Bean
	public TaskExecutor executor() {
		SimpleAsyncTaskExecutor exeAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
		exeAsyncTaskExecutor.setConcurrencyLimit(2);
		return exeAsyncTaskExecutor;
	}

	@Bean
	public Step step(ItemReader<Product> reader, ItemProcessor<Product, Product> processor, ItemWriter<Product> writer,
			JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("myStep", jobRepository).<Product, Product>chunk(10, transactionManager).reader(reader)
				.processor(processor).writer(writer).build();
	}
}