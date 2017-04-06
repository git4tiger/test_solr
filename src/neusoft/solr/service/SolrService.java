package neusoft.solr.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

public interface SolrService {
	String RESULT_TOTALCOUNT = "totalCount";
	String RESULT_PAGENO = "pageNo";
	String RESULT_PAGESIZE = "pageSize";
	String RESULT_DATA = "data";

	List<String> query(String coreName, SolrQuery query) throws SolrServerException;

	void deleteById(String coreName, String id) throws SolrServerException, IOException;

	void deleteByQuery(String coreName, String query) throws SolrServerException, IOException;

	void add(String coreName, SolrInputDocument document) throws SolrServerException, IOException;

	void add(String coreName, AddJob job, int queueSize, int threadCount) throws SolrServerException, IOException;
}
