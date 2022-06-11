package com.springboot.betterreads.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.springboot.betterreads.repository")
public class CassandraConfig extends AbstractCassandraConfiguration{
	
	@Value("${spring.data.cassandra.local-datacenter}")
	private String localDataCenter;
	@Value("${spring.data.cassandra.contact-points}")
	private String hosts;
	@Value("${spring.data.cassandra.keyspace-name}")
	private String keyspace;
	//@Value("${spring.data.cassandra.schema-action}")
	//private final String schemaAction;

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
	}
	
	@Override
	protected String getKeyspaceName() {
		return this.keyspace;
	}
	
	@Override
	public List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keyspace)
				.with(KeyspaceOption.DURABLE_WRITES, true)
				.ifNotExists();
		
		return Arrays.asList(specification);
	}
	
	@Override
	protected String getLocalDataCenter() {
	    return localDataCenter;
	}
	
	@Override
	protected String getContactPoints() {
	    return hosts;
	}
	
	@Override
	public String[] getEntityBasePackages() {
	    return new String[]{"com.springboot.betterreads"};
	}
}
