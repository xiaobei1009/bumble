/*
 * Copyright 2018-Present Hikvision Inc.
 * 
 */
package org.bumble.registry;

import java.util.List;

import org.bumble.registry.data.MngrNode;
import org.bumble.registry.data.RegistryData;

/**
 * 
 * Registry API
 * <p>
 * There are 3 kind of roles who will connect to this registry.<br>
 * 1. Transaction manager: who will take responsibility for reconciling the transactions.<br>
 * 2. Client module: who will execute the specific business logic and rely on the transaction manager.<br>
 * 3. Monitor manager: who will get the full picture of these invocation chain, <br>
 * 					 the details of the invocation status,(etc transaction Q/S & machine performance weight) <br>
 * 					 and can perform certain management functionality.(etc autoReBalance & forceReBalance)<br>
 * 
 * @author shenxiangyu
 *
 */
public interface Registry {
	
	public final String CONFIG_NS = "bumble.registry";
	
	/**
	 * Get registry data
	 * 
	 * @return RegistryData
	 */
	public RegistryData getData();
	
	/**
	 * Clear all the data
	 */
	public void clear();
	
	/**
	 * Register a transaction manager to the registry
	 * <p>
	 * It will be called by the transaction manager itself to register it to the registry<br>
	 * <br>
	 * The getLocalHost ip address will be used for ip<br>
	 * The port will be retrieved from bumble-config file under specific key[bumble.manager.server.port]<br>
	 * 
	 */
	public void register();
	
	/**
	 * Register a transaction manager to the registry
	 * <p>
	 */
	public void register(String mngrName, String mngrUrl);
	
	/**
	 * Unregister a transaction manager in the registry
	 * <p>
	 * 1. It will be called by monitor manager if it is going to be unloaded from the network<br>
	 * 2. It will be called by the client module when it found that the connected transaction manager is dead<br>
	 * 
	 * @param name
	 */
	public void unregister(String name);
	
	/**
	 * Get an eligible transaction manager for a client 
	 * <p>
	 * 1. It will be called by the client module on initialization<br>
	 * 2. If connected transaction manager is dead, unregister will be called by the client module<br>
	 *    first, then this will be called to retrieve a new one<br>
	 * 
	 * @return MngrNode(manager name)
	 */
	public MngrNode getMngr4Client();
	
	/**
	 * <pre>
	 * Get an eligible transaction manager for a client 
	 * which is not in the exceptNameList
	 * </pre>
	 * @return {@link org.bumble.registry.data.MngrNode}
	 * @see org.bumble.registry.Registry#getMngr4Client
	 */
	public MngrNode getMngr4Client(List<String> exceptNameList);
	
	/**
	 * Bind a client module to a transaction manager
	 * <p>
	 * It will be called by the transaction manager if it found a client module is arrived<br>
	 * 
	 * @param mngrName
	 */
	public void bindClientToManager(String mngrName);
	
	/**
	 * Bind a client module to a transaction manager
	 * <p>
	 * @param clientName
	 * @param clientUrl
	 * @param mngrName
	 */
	public void bindClientToManager(String clientName, String clientUrl, String mngrName);
	
	/**
	 * <pre>
	 * Unbind a client module from a transaction manager
	 * 
	 * 1. It will be called by the client module if it found that the transaction manager is dead
	 * 2. It will be called by the transaction manager if it found that the client module is dead
	 * <pre>
	 * @param clientUuid
	 * @param mngrUrl
	 */
	public void unbindClientFromManager(String clientName, String mngrName);
	
	/**
	 * <pre>
	 * TODO reload will be processed by leader role
	 * 
	 * Re-register & re-bind the previous managers & clients
	 * If the registry server is changed
	 * </pre>
	 * @param registryData
	 */
	public void reload(RegistryData registryData);
	
	/**
	 * <pre>
	 * Watch on the registry, and will be called back with new registry data when 
	 * the registry is changed
	 * </pre>
	 * @param registryChangedNotifier
	 */
	public void watchOnRegistry(RegistryChangedNotifier registryChangedNotifier);
	
	/**
	 * Exit
	 */
	public void exit();
}
