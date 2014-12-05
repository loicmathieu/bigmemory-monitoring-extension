BigMemory Monitoring Extension
==============================

This extension works only with the standalone machine agent.

## Use Case

BigMemory is an enterprise distributed cache made by Terracotta (a division of Software AG) on top of the well known Ehcache framework (which is a standards-based cache for boosting performance, offloading your database, and simplifying scalability).

The bigmemory-monitoring extension gathers metrics for a specific BigMemory server (aka BigMemory Max) and sends them to the AppDynamics Metric Browser. 
It's important that all the nodes of the cluster (the server array) use the extension.

It has been tested with both BigMemory Max version 4.0.x and 4.1.x.

## Installation
<ol>
	<li>Gather the latest BigMemoryMonitor-x.x.x zip file from the [releases](https://github.com/loicmathieu/bigmemory-monitoring-extension/releases) page</li>
	<li>Deploy it into the &lt;machineagent install dir&gt;/monitors/ directory.</li>
	<li>Unzip the deployed file.</li>
	<li>
		Open &lt;machineagent install dir&gt;/monitors/BigMemoryMonitor/monitor.xml and configure the BigMemory parameters.
		<p></p>
		<pre>
			&lt;argument name="host" is-required="true" default-value="localhost" /&gt;          
			&lt;argument name="port" is-required="true" default-value="9250" /&gt;
		</pre>
	</li>	
	<li> Restart the machine agent.</li>
	<li>In the AppDynamics Metric Browser, look for: Application Infrastructure Performance | &lt;Tier&gt; | Custom Metrics | Cache | BigMemory</li>
</ol>

## ServerArray metrics
Statistic metrics are available under the ServerArray|Statistics path.

|Metric Name           | Description     |
|----------------------|-----------------|
|EvictionRate    	   | Rate of evictions from the cache |
|ExpirationRate        | Rate of expiratpion from the cache |
|ReadOperationRate     | Rate of read operation (get) |
|WriteOperationRate    | Rate of write operation (put, remove) |
|LiveObjectCount       | Number of objects in the cache |
|OffheapMaxSize        | The maximum size of the offheap store |
|OffheapReservedSize   | The reserved size of the offheap store |
|OffheapUsedSize       | The currently used size of the offheap store |

health metrics are available under the ServerArray|Health path :

|Metric Name           | Description     |
|----------------------|-----------------|
|Active	   		|1 if the node is active (master), else 0 |
|Passive		|1 if the node is passive (mirror), else 0 |
|Healthy		|Check the state of the node, if active check that it's ACTIVE-COORDINATOR and if passive that it's PASSIVE-STANDBY. If OK return 1 else 0 |
|up    	   		|Wether or not the node is up : 1 means up else 0 |
|MaxHeapMemory	|Maximum heap memory (Java heap memory, not reporting native memory so not reporting offheap memory) |
|UsedHeapMemory	|User heap memory (Java heap memory, not reporting native memory so not reporting offheap memory) |
|ActiveClientCount	|Number of active clients |

##Contributing

Always feel free to fork and contribute any changes directly via [GitHub](https://github.com/loicmathieu/bigmemory-monitoring-extension).

## Directory Structure

| Directory/File | Description |
|----------------|-------------|
|conf            | Contains the monitor.xml |
|lib             | Contains third-party project references |
|src             | Contains source code of the BigMemory monitoring extension |
|dist            | Only obtained when using ant. Run 'ant build' to get binaries. Run 'ant package' to get the distributable .zip file |
|build.xml       | Ant build script to package the project (required only if changing Java code) |

## Building
<ol>
	<li>Get the source from Github ...</li>
	<li>Type 'ant package' in the command line from the bigmemory-monitoring-extension directory.</li>
	<li>It will create the file BigMemoryMonitor.zip in the 'dist' directory.</li>
<ol>