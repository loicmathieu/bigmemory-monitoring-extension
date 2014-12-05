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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;


public class BigMemoryJMXWrapper {
	/*
	 * Object Name naming schema from Terracotta documentation :
	 * CacheManager - "net.sf.ehcache:type=CacheManager,name=<CacheManager>"
	 * Cache - "net.sf.ehcache:type=Cache,CacheManager=<cacheManagerName>,name=<cacheName>"
	 * CacheConfiguration - "net.sf.ehcache:type=CacheConfiguration,CacheManager=<cacheManagerName>,name=<cacheName>"
	 * CacheStatistics - "net.sf.ehcache:type=CacheStatistics,CacheManager=<cacheManagerName>,name=<cacheName>"
	 */
	public Map<String, Number> gatherGlobalMetrics(String host, String port) throws IOException, JMException {
		Map<String, Number> metrics = new HashMap<String, Number>();
		JMXConnector connector = null;

		try {
			connector = JmxUtils.connect(host, port, null, null);
			MBeanServerConnection connection = connector.getMBeanServerConnection();

			//gather cluster stats
			ObjectName bigMemory = new ObjectName("org.terracotta:type=Terracotta Server,name=DSO");

			//Rates : EvictionRate(long), ExpirationRate(long), ReadOperationRate(long), WriteOperationRate(long), TransactionRate(long), TransactionSizeRate(long)
			Long evictionRate = (Long) connection.getAttribute(bigMemory, "EvictionRate");
			metrics.put("EvictionRate", evictionRate);
			Long expirationRate = (Long) connection.getAttribute(bigMemory, "ExpirationRate");
			metrics.put("ExpirationRate", expirationRate);
			Long readRate = (Long) connection.getAttribute(bigMemory, "ReadOperationRate");
			metrics.put("ReadOperationRate", readRate);
			Long writeRate = (Long) connection.getAttribute(bigMemory, "WriteOperationRate");
			metrics.put("WriteOperationRate", writeRate);

			//Sizes : LiveObjectCount(int), OffheapMaxSize(long), OffheapReservedSize(long), OffheapUsedSize(long)
			Integer liveObjectCount = (Integer) connection.getAttribute(bigMemory, "LiveObjectCount");
			metrics.put("LiveObjectCount", liveObjectCount);
			Long offheapMaxSize = (Long) connection.getAttribute(bigMemory, "OffheapMaxSize");
			metrics.put("OffheapMaxSize", offheapMaxSize);
			Long offheapReservedSize = (Long) connection.getAttribute(bigMemory, "OffheapReservedSize");
			metrics.put("OffheapReservedSize", offheapReservedSize);
			Long offheapUsedSize = (Long) connection.getAttribute(bigMemory, "OffheapUsedSize");
			metrics.put("OffheapUsedSize", offheapUsedSize);

			return metrics;
		}
		finally  {
			if(connector != null){
				connector.close();
			}
		}
	}
}
