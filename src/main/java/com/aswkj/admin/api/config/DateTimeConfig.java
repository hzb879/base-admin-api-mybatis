package com.aswkj.admin.api.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class DateTimeConfig {

  @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
  private String DEFAULT_DATE_TIME_FORMAT;

  private String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

  private String DEFAULT_TIME_FORMAT = "HH:mm:ss";

  /**
   * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
   */
  @Bean
  public Converter<String, LocalDateTime> localDateTimeConverter() {
    return new Converter<String, LocalDateTime>() {
      @Override
      public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
      }
    };
  }

  /**
   * LocalDate转换器，用于转换RequestParam和PathVariable参数
   */
  @Bean
  public Converter<String, LocalDate> localDateConverter() {
    return new Converter<String, LocalDate>() {
      @Override
      public LocalDate convert(String source) {
        return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
      }
    };
  }


  /**
   * LocalTime转换器，用于转换RequestParam和PathVariable参数
   */
  @Bean
  public Converter<String, LocalTime> localTimeConverter() {
    return new Converter<String, LocalTime>() {
      @Override
      public LocalTime convert(String source) {
        return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
      }
    };
  }

  /**
   * Date转换器，用于转换RequestParam和PathVariable参数
   */
  @Bean
  public Converter<String, Date> dateConverter() {
    return new Converter<String, Date>() {
      @Override
      public Date convert(String source) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        try {
          return format.parse(source);
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }


  @Bean
  public LocalDateTimeSerializer localDateTimeSerializer() {
    return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
  }

  @Bean
  public LocalDateTimeDeserializer localDateTimeDeserializer() {
    return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
  }


  @Bean
  public LocalDateSerializer localDateSerializer() {
    return new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
  }

  @Bean
  public LocalDateDeserializer localDateDeserializer() {
    return new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
  }

  @Bean
  public LocalTimeSerializer localTimeSerializer() {
    return new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
  }

  @Bean
  public LocalTimeDeserializer localTimeDeserializer() {
    return new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
  }

  @Bean
  public JavaTimeModule javaTimeModule() {

    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer());
    javaTimeModule.addSerializer(LocalDate.class, localDateSerializer());
    javaTimeModule.addSerializer(LocalTime.class, localTimeSerializer());
    javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer());
    javaTimeModule.addDeserializer(LocalDate.class, localDateDeserializer());
    javaTimeModule.addDeserializer(LocalTime.class, localTimeDeserializer());

    return javaTimeModule;
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> builder.modulesToInstall(javaTimeModule());
  }
}
