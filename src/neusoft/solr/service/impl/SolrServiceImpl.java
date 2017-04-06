package neusoft.solr.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import neusoft.solr.service.AddJob;
import neusoft.solr.service.SolrService;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrServiceImpl  implements SolrService{
	private final Map<String, SolrServer> CONNECTIONS = new HashMap<String, SolrServer>();

	private String solrServerAddress;

	public void afterPropertiesSet() throws Exception {
		final Properties config = new Properties();
		InputStream is=SolrServiceImpl.class.getClassLoader().getResourceAsStream("solr.properties");
		config.load(is);
		solrServerAddress = config.getProperty("server");
		if (!solrServerAddress.endsWith("/")) {
			solrServerAddress += "/";
		}
	}


	private synchronized SolrServer getConnectionForQuery(String coreName) {
		HttpSolrServer connection = (HttpSolrServer) CONNECTIONS.get(coreName);
		if (connection == null) {
			connection = new HttpSolrServer(solrServerAddress + coreName);
			CONNECTIONS.put(coreName, connection);
			connection.setSoTimeout(1000);
			connection.setConnectionTimeout(1000);
			connection.setAllowCompression(true);
			connection.setMaxRetries(1);
		}
		return connection;
	}

	@Override
	public List<String> query(String coreName, SolrQuery query) throws SolrServerException {
		final List<String> result = new ArrayList<String>();
		final SolrServer connection = getConnectionForQuery(coreName);
		final QueryResponse queryResponse = connection.query(query);
		final SolrDocumentList docResults = queryResponse.getResults();
		for (int i = 0; i < docResults.size(); i++) {
			final SolrDocument doc = docResults.get(i);
			result.add((String) doc.getFieldValue("id"));
		}
		return result;
	}

	@Override
	public void deleteByQuery(String coreName, String query) throws SolrServerException, IOException {
		final SolrServer connection = getConnectionForQuery(coreName);
		connection.deleteByQuery(query);
		connection.commit();
	}

	@Override
	public void deleteById(String coreName, String id) throws SolrServerException, IOException {
		final SolrServer connection = getConnectionForQuery(coreName);
		connection.deleteById(id);
		connection.commit();
	}

	@Override
	public void add(String coreName, SolrInputDocument document) throws SolrServerException, IOException {
		final SolrServer connection = getConnectionForQuery(coreName);
		connection.add(document);
		connection.commit();
	}

	@Override
	public void add(String coreName, AddJob job, int queueSize, int threadCount) throws SolrServerException, IOException {
		final ConcurrentUpdateSolrServer connection = new ConcurrentUpdateSolrServer(solrServerAddress + coreName, queueSize, threadCount);
		connection.setSoTimeout(1000);
		connection.setConnectionTimeout(1000);
		try {
			job.doInSolr(connection);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.shutdown();
		}
	}
}
