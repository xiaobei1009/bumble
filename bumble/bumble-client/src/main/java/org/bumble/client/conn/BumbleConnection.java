package org.bumble.client.conn;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.bumble.client.action.ClientActionBuilder;
import org.bumble.client.remoting.RemotingTransporterFactory4BClient;
import org.bumble.client.threadlocal.TxnThreadLocal;
import org.bumble.core.action.ActionConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BumbleConnection implements Connection {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Connection connection;
	
	public static Connection getConnection(Connection connection) {
		Connection conn = new BumbleConnection(connection);
		return conn;
	}
	
	public BumbleConnection(Connection connection) {
		this.connection = connection;
		isClosing.set(false);
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return connection.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return connection.isWrapperFor(iface);
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		BumblePreparedStatement bps = new BumblePreparedStatement(ps, sql);
		return bps;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return connection.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return connection.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		RemotingTransporterFactory4BClient client = RemotingTransporterFactory4BClient.getInstance();
		
		if (client.isConnected()) {
			connection.setAutoCommit(false);
		} else {
			connection.setAutoCommit(autoCommit);
		}
	}

	public boolean getAutoCommit() throws SQLException {
		return connection.getAutoCommit();
	}
	
	private ThreadLocal<Boolean> isClosing = new ThreadLocal<Boolean>();
	
	public void commit() throws SQLException {
		if (TxnThreadLocal.getCurrent() == null) {
			// The cluster transaction is not enabled, commit the transaction directly
			connection.commit();
			return;
		}
		sendTransactionStateMsg(ActionConst.Type.TXN_SUCCESS);
	}
	
	/**
	 * Send message to bumble manager with transaction status. (Success or Failed)
	 * <p>
	 * @param actionType
	 */
	private void sendTransactionStateMsg(String actionType) {
		RemotingTransporterFactory4BClient client = RemotingTransporterFactory4BClient.getInstance();
		
		String message = ClientActionBuilder.getInstance().buildActionMsg(actionType);
		client.sendMsg(message);
	}
	
	public void rollback() throws SQLException {
		sendTransactionStateMsg(ActionConst.Type.TXN_FAIL);
		connection.rollback();
	}
	
	public void close() throws SQLException {
		logger.debug("Intercept before connection close");
		if (isClosing.get()) {
			logger.error("Should not get here! Need further investigation!!!");
			return;
		}
		isClosing.set(true);
		if (TxnThreadLocal.getCurrent() == null) {
			// The cluster transaction is not enabled, close the transaction directly
			connection.close();
			return;
		}
		
		BumbleConnectionCloseProcessor.getInstance().process(connection);
	}
	
	public boolean isClosed() throws SQLException {
		return connection.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		connection.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return connection.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		connection.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return connection.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return connection.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return connection.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		connection.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return connection.createStatement();
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return connection.getTypeMap();
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		connection.setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException {
		connection.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return connection.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return connection.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return connection.setSavepoint();
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		connection.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return connection.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return connection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return connection.prepareStatement(sql, columnNames);
	}

	public Clob createClob() throws SQLException {
		return connection.createClob();
	}

	public Blob createBlob() throws SQLException {
		return connection.createBlob();
	}

	public NClob createNClob() throws SQLException {
		return connection.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return connection.createSQLXML();
	}

	public boolean isValid(int timeout) throws SQLException {
		return connection.isValid(timeout);
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		connection.setClientInfo(name, value);
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		connection.setClientInfo(properties);
	}

	public String getClientInfo(String name) throws SQLException {
		return connection.getClientInfo(name);
	}

	public Properties getClientInfo() throws SQLException {
		return connection.getClientInfo();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return connection.createArrayOf(typeName, elements);
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return connection.createStruct(typeName, attributes);
	}

	public void setSchema(String schema) throws SQLException {
		connection.setSchema(schema);
	}

	public String getSchema() throws SQLException {
		return connection.getSchema();
	}

	public void abort(Executor executor) throws SQLException {
		connection.abort(executor);
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		connection.setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException {
		return connection.getNetworkTimeout();
	}

}
