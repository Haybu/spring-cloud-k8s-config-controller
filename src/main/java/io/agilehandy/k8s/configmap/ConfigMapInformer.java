/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.agilehandy.k8s.configmap;

import javax.annotation.PreDestroy;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * @author Haytham Mohamed
 **/

@Component
public class ConfigMapInformer {

	private static Logger logger =
			LoggerFactory.getLogger(ConfigMapInformer.class);

	private final ConfigMapEventHandler handler;
	private final SharedInformerFactory factory;

	public ConfigMapInformer(ConfigMapEventHandler handler
			, SharedInformerFactory factory) {
		this.handler = handler;
		this.factory = factory;
	}

	public void run() {
		SharedIndexInformer<ConfigMap> informer =
				factory.getExistingSharedIndexInformer(ConfigMap.class);
		informer.addEventHandler(handler);
		logger.info("Starting all registered informers");
		factory.startAllRegisteredInformers();
	}

	@PreDestroy
	public void destroy() {
		logger.info("Stopping all registered informers");
		factory.stopAllRegisteredInformers();
	}

}
