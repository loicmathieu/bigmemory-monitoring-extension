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

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

/**
 * helper class permettant d'etablir une connection JMX
 *
 * @author lmathieu
 *
 */
public final class JmxUtils {
	private static final Logger logger = Logger.getLogger(JmxUtils.class.getName());

	/**
	 * Constructeur privé : pattern helper class
	 */
	private JmxUtils() {
		//constructeur vide
	}


	/**
	 * Etablit une connexion JMX
	 * @param host
	 * @param port
	 * @param login
	 * @param password
	 * @return une connexion au serveur JMX
	 * @throws IOException
	 */
	public static JMXConnector connect(String host, String port, String login, String password) throws IOException{
		String jmxUrl = "service:jmx:jmxmp://" + host + ":" + port;
		logger.info("Connecting to BigMemory Server on " + jmxUrl);

		JMXServiceURL serviceUrl = new JMXServiceURL(jmxUrl);
		Map<String, Object> env = new HashMap<String, Object>();
		if(login != null && password != null){
			String[] creds = {login, password};
			env.put(JMXConnector.CREDENTIALS, creds);
		}


		return JMXConnectorFactory.connect(serviceUrl, env);
	}

}
