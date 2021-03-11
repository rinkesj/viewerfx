package com.dere.viewerfx.parser;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.dere.viewerfx.api.IDataFileParser;

@ApplicationScoped
public class ParserFactory {
	
	@Inject
	private Instance<IDataFileParser> viewPlugins;
	private Map<String, IDataFileParser> cache;
	
	@PostConstruct
	private void buildCache(){
		cache = viewPlugins.stream().collect(Collectors.toMap(IDataFileParser::type,Function.identity()));
	}

	public IDataFileParser getByType(String type) {
		return cache.get(type);
	}

}
