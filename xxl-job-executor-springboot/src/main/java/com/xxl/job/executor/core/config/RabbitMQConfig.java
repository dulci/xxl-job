package com.xxl.job.executor.core.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String trigger_after_update_analysised_ddid_key = "trigger_after_update_analysised_crawler_project_ddid_key";
	public static final String trigger_after_update_analysised_id_key = "trigger_after_update_analysised_crawler_project_id_key";

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter());
		template.setExchange("my-mq-exchange");
		return template;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter());
		return factory;
	}

	@Bean
	public MessageConverter messageConverter() {
		return new ContentTypeDelegatingMessageConverter(new FastJsonMessageConverter());
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("my-mq-exchange");
	}

	@Bean
	public Queue createTriggerUpdateAnalysisedByDdidQueue() {
		return new Queue("trigger_after_update_analysised_crawler_project_ddid_queue");
	}

	@Bean
	public Binding bindingTriggerUpdateAnalysisedByDdidueue() {
		return BindingBuilder.bind(createTriggerUpdateAnalysisedByDdidQueue()).to(directExchange()).with(trigger_after_update_analysised_ddid_key);
	}

	@Bean
	public Queue createTriggerUpdateAnalysisedByIdQueue() {
		return new Queue("trigger_after_update_analysised_crawler_project_id_queue");
	}

	@Bean
	public Binding bindingCreateInfoTaskQueue() {
		return BindingBuilder.bind(createTriggerUpdateAnalysisedByIdQueue()).to(directExchange()).with(trigger_after_update_analysised_id_key);
	}

}
