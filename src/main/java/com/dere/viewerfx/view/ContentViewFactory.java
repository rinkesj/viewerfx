package com.dere.viewerfx.view;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.dere.viewerfx.api.IContentView;

@ApplicationScoped
public class ContentViewFactory {
	
	@Inject
	private Instance<IContentView> viewPlugins;
	private Map<String, IContentView> cache;
	
	@PostConstruct
	private void buildCache(){
		cache = viewPlugins.stream().collect(Collectors.toMap(IContentView::type,Function.identity()));
	}

	public IContentView getByType(String type) {
		return cache.get(type);
	}

}
