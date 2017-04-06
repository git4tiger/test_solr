package neusoft.solr.test;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import neusoft.solr.service.impl.SolrServiceImpl;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//delete();
		add();
	}
	
	
	public static void query() throws Exception{
		SolrQuery sq=new SolrQuery("*:*");
		
		SolrServiceImpl ss=new SolrServiceImpl();
		ss.afterPropertiesSet();
		List<String> resultList=ss.query("resume_njrc", sq);
		System.out.println(resultList);
	}
	
	public static void add() throws Exception{

		
		SolrServiceImpl ss=new SolrServiceImpl();
		ss.afterPropertiesSet();
		
		SolrInputDocument doc=new SolrInputDocument();
		
		doc.addField("id",3);
		doc.addField("weight",23);
		doc.addField("AAC003", "Сʦ��");
		doc.addField("links", "����");
		doc.addField("links", "ˬ��");
		doc.addField("links", "����");
		
		
		ss.add("resume_nnkb", doc);
	}
	
	public static void delete() throws Exception{
		SolrServiceImpl ss=new SolrServiceImpl();
		ss.afterPropertiesSet();
		
		ss.deleteById("job_nnkb", "626555");
	}

}
