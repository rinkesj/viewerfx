package com.dere.viewerfx.formatter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.dere.viewerfx.api.IDataContentFormatter;

@ApplicationScoped
public class FormatterFactory {
	
	@Inject
	private Instance<IDataContentFormatter> viewPlugins;
	private Map<String, IDataContentFormatter> cache;
	
	@PostConstruct
	private void buildCache(){
		cache = viewPlugins.stream().collect(Collectors.toMap(IDataContentFormatter::type,Function.identity()));
	}

	public IDataContentFormatter getByType(String type) {
		return cache.getOrDefault(type, new NoDataContentFormatter());
	}
}
