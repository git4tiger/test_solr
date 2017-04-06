package neusoft.solr.service;

import org.apache.solr.client.solrj.SolrServer;

public interface AddJob {
	void doInSolr(SolrServer connection);
}
