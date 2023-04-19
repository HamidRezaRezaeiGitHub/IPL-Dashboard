package com.hrr.ipldashboard.data;

import com.hrr.ipldashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    public BatchConfig(DataSource dataSource, JobCompletionNotificationListener listener, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        logger.info("Batch Config instantiated.");
        this.dataSource = dataSource;
        this.listener = listener;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
    private static final String[] FIELD_NAMES = new String[]{
            "id",
            "city",
            "date",
            "player_of_match",
            "venue",
            "neutral_venue",
            "team1",
            "team2",
            "toss_winner",
            "toss_decision",
            "winner",
            "result",
            "result_margin",
            "eliminator",
            "method",
            "umpire1",
            "umpire2"
    };
    public final DataSource dataSource;
    public final JobCompletionNotificationListener listener;
    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;


    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        logger.info("Reader bean is starting ...");
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("MatchItemReader")
                .resource(new ClassPathResource("match-data.csv"))
                .delimited()
                .names(FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(MatchInput.class);
                    }
                })
                .linesToSkip(1)
                .build();
    }

    @Bean
    public MatchDataProcessor processor() {
        logger.info("Processor bean is starting ...");
        return new MatchDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Match> writer() {
        logger.info("Writer bean is starting ...");
        return new JdbcBatchItemWriterBuilder<Match>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO match " +
                        "(id, city, date, player_of_match, venue, team1, team2, toss_winner, toss_decision, winner, result, result_margin, umpire1, umpire2) " +
                        "VALUES (:id, :city, :date, :playerOfMatch, :venue, :team1, :team2, :tossWinner, :tossDecision, :winner, :result, :resultMargin, :umpire1, :umpire2)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step1() {
        logger.info("Step1 bean is starting ...");
        return stepBuilderFactory
                .get("step1")
                .<MatchInput, Match>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job1() {
        logger.info("job1 bean is starting ... - Listener: " + listener.hashCode());
        return jobBuilderFactory
                .get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .listener(listener)
                .build();
    }

}
