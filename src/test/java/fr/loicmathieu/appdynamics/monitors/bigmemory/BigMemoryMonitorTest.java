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


import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;


public class BigMemoryMonitorTest {
	private BigMemoryMonitor monitor = new BigMemoryMonitor();

	@Test
	public void testExecute() throws TaskExecutionException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("host", "xetu03");
		params.put("port", "9520");

		TaskOutput output = monitor.execute(params, null);

		Assert.assertNotNull(output);
		Assert.assertEquals("Task successful...", output.getStatusMessage());
	}

}
