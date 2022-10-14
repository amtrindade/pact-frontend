package br.ce.wcaquino.taskfrontend.pact.consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.junit.Rule;
import org.junit.Test;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.tasksfrontend.model.Todo;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRepository;

public class GetTaskByIdTest {
	
	@Rule
	public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);
	
	@Pact(consumer = "TasksFront")
	public RequestResponsePact createPact(PactDslWithProvider builder) {
		DslPart body = new PactDslJsonBody()
				.numberType("id", 1L)
				.stringType("task")
				.date("dueDate", "yyyy-MM-dd", new Date());		
						
		return builder				
				.given("There is a task with id = 1")
				.uponReceiving("Retrieve Task #1")
					.matchPath("/todo/\\d+", "/todo/1")
					.method("GET")
				.willRespondWith()
					.status(200)
					.body(body)
				.toPact();
	}
	
	@Test
	@PactVerification
	public void test() throws ClientProtocolException, IOException {
		//Arrange
		TasksRepository consumer = new TasksRepository(mockProvider.getUrl());
		
		//Act
		Todo task = consumer.getTodo(1L);
		System.out.println(task);
		
		//Assert
		assertThat(task.getId(), is(notNullValue()));
		assertThat(task.getId(), is(1L));
		assertThat(task.getTask(), is(notNullValue()));
		assertThat(task.getDueDate(), is(LocalDate.now()));
	}
}
