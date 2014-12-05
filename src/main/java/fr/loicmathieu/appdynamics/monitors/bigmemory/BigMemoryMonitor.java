/**
 * Copyright 2014 Loïc Mathieu : loicmathieu@free.fr - @loicmathieu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.loicmathieu.appdynamics.monitors.bigmemory;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

/**
 * Appdynamics BigMemory Monitor
 *
 * @author lmathieu
 *
 */
public class BigMemoryMonitor extends AManagedMonitor {
	private static final Logger logger = Logger.getLogger(BigMemoryMonitor.class.getName());

	private static final String ROOT_PREFIX = "Custom Metrics|Cache|BigMemory|";
	private static final String CLUSTER_PREFIX = "ServerArray|";
	private static final String GLOBAL_STATS_PREFIX = CLUSTER_PREFIX + "Statistics|";
	private static final String GLOBAL_HEALTH_PREFIX = CLUSTER_PREFIX + "Health|";

	/**
	 * @inheritDoc
	 */
	public TaskOutput execute(Map<String, String> params, TaskExecutionContext context) throws TaskExecutionException {
		logger.info("Executing BigMemory Monitor...");
		BigMemoryJMXWrapper wrapper = new BigMemoryJMXWrapper();
		try {
			Map<String, Number> globalMetrics = wrapper.gatherGlobalMetrics(params.get("host"), params.get("port"));
			logger.info("Gathered global metrics successfully. Size of metrics: " + globalMetrics.size());
			sendIndividualMetrics(GLOBAL_STATS_PREFIX, globalMetrics);

			Map<String, Number> healthMetrics = wrapper.gatherHealthMetrics(params.get("host"), params.get("port"));
			logger.info("Gathered health metrics successfully. Size of metrics: " + healthMetrics.size());
			sendCollectiveMetrics(GLOBAL_HEALTH_PREFIX, healthMetrics);

			//TODO add stats for all caches

			logger.info("Printed metrics successfully");
			return new TaskOutput("Task successful...");
		}
		catch (Exception e) {
			logger.error("Exception while exucuting BigMemory Monitor : ", e);
		}

		return new TaskOutput("Task failed with errors");
	}


	/**
	 * Iterate over all metrics and send them to appdynamics controller for individual metrics
	 *
	 * @param metrics
	 * @param prefix prefix name for the metric
	 */
	private void sendIndividualMetrics(String prefix, Map<String, Number> metrics) {
		System.out.println(metrics);
		for(Entry<String, Number> entry : metrics.entrySet()){
			String name = entry.getKey();
			Number value = entry.getValue();
			sendMetric(prefix + name , value, MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
					MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
					MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_INDIVIDUAL);
		}
	}


	/**
	 * Iterate over all metrics and send them to appdynamics controller for collective metrics
	 *
	 * @param metrics
	 * @param prefix prefix name for the metric
	 */
	private void sendCollectiveMetrics(String prefix, Map<String, Number> metrics) {
		System.out.println(metrics);
		for(Entry<String, Number> entry : metrics.entrySet()){
			String name = entry.getKey();
			Number value = entry.getValue();
			sendMetric(prefix + name , value, MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
					MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
					MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);
		}
	}


	/**
	 * Returns the metric to the AppDynamics Controller.
	 *
	 * @param 	metricName		Name of the Metric
	 * @param 	metricValue		Value of the Metric
	 * @param 	aggregation		Average OR Observation OR Sum
	 * @param 	timeRollup		Average OR Current OR Sum
	 * @param 	cluster			Collective OR Individual
	 */
	private void sendMetric(String metricName, Number metricValue, String aggregation, String timeRollup, String cluster) {
		MetricWriter metricWriter = super.getMetricWriter(ROOT_PREFIX + metricName, aggregation, timeRollup, cluster);
		metricWriter.printMetric(String.valueOf((long) metricValue.doubleValue()));
	}

}
