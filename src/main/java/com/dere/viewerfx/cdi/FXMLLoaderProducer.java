package com.dere.viewerfx.cdi;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.proxy.WeldClientProxy;

import com.dere.viewerfx.log.ViewerFxLogMessages;

import javafx.fxml.FXMLLoader;

public class FXMLLoaderProducer {

	private final Logger LOGGER = LogManager.getLogger(FXMLLoaderProducer.class.getSimpleName());

	@Inject
	private Instance<Object> instance;

	@Produces
	public FXMLLoader createLoader() {
		FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory(param -> extracted(param));
		return loader;
	}

	private Object extracted(Class<?> controllerClass) {
		Object controllerBean = instance.select(controllerClass).get();

		// check if controller has expected annotation
		if (!(!controllerClass.isAnnotationPresent(ApplicationScoped.class) && controllerClass.isAnnotationPresent(Singleton.class)) // is not application scoped
				&& !(controllerClass.isAnnotationPresent(ApplicationScoped.class) && !controllerClass.isAnnotationPresent(Singleton.class))) { // is not singletone
			LOGGER.warn(ViewerFxLogMessages.FXML_LOADER_NO_APPLICATION_SCOPED.log(controllerClass, Arrays.toString(controllerClass.getClass().getAnnotations())));
		}
		
		// if weld proxy then return real instance to JavaFx for FXML injects
		if (isProxy(controllerBean)) {
			WeldClientProxy proxyBean = (WeldClientProxy) controllerBean;
			return proxyBean.getMetadata().getContextualInstance();
		}
		return controllerBean;
	}

	public boolean isProxy(Object obj) {
		try {
			return Class.forName(WeldClientProxy.class.getName()).isInstance(obj);
		} catch (Exception e) {
			LOGGER.warn("Unable to check if object is proxy", e);
		}
		return false;
	}
}
