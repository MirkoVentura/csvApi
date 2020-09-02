package it.mventura.demo;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@SpringBootApplication
public class CvsToJsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CvsToJsonApplication.class, args);
	}

	@Autowired
	private Environment env;
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			//reading from config file.
			String inputPath = env.getProperty("inputPath");
			String outputPath = env.getProperty("outputPath");

			//File f = new File(outputPath);
			//f.createNewFile();

			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			System.out.println("Current relative path is: " + s);

			System.out.println("Let's convert csv file from "+inputPath+" into json stored in "+outputPath);
			//File input = ResourceUtils.getFile(s+"/"+inputPath);
			File input = new File(inputPath);
			File output = new File(outputPath);
			try {
				CsvSchema csv = CsvSchema.emptySchema().withHeader();
				CsvMapper csvMapper = new CsvMapper();
				MappingIterator<Map<?, ?>> mappingIterator =  csvMapper.reader().forType(Map.class).with(csv).readValues(input);
				List<Map<?, ?>> data = mappingIterator.readAll();
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(output, data);
			} catch(Exception e) {
				System.out.println("Exception catched "+e.getLocalizedMessage());
				e.printStackTrace();
			}


		};
	}
}
