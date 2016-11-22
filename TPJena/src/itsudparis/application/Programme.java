package itsudparis.application;

import java.util.Scanner;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

import itsudparis.tools.JenaEngine;

public class Programme {

	public static final String ns = "http://www.semanticweb.org/abdelhadikawtar/ontologies/2016/10/untitled-ontology-3#";
    public static String inputDataOntology = "data/FamilleTP2.owl";
    public static String inputRule = "data/rules.txt";
    public static String inputQuery = "data/marie.txt";
    public static Model inferedModel;
    public static String entete = "PREFIX ns: <http://www.semanticweb.org/abdelhadikawtar/ontologies/2016/10/untitled-ontology-3#>"
    							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
    							+  "PREFIX owl: <http://www.w3.org/2002/07/owl#>"; 
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String nom="";
		Scanner sc  = new Scanner(System.in);
		System.out.println("MENU FAMILY");
		System.out.println("-----------------------------");
		init();
		
		while(!nom.equals("q"))
		{
			System.out.println("Saisissez un nom (ou q pour quitter) : ");
			nom=sc.nextLine();
			if(exists(nom))
			{
				afficherParents(nom);
				afficherFreres(nom);
				afficherMaris(nom);
				afficherEnfants(nom);
			}
			else
			{
				System.out.println("This person doesn't exist ! ");
			}
			
		}
	}
	
	public static void init()
	{
		 Model model = JenaEngine.readModel(inputDataOntology);
	     inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, inputRule);
	}
	
	public static void afficherParents(String nom)
	{
		String query = entete + "SELECT ?user ?parent "
				+ "WHERE { "
				+ "?user ns:nom \"" + nom  + "\"."
				+ "{?user ns:estFilsDe ?parent} "
				+ "UNION"
				+ "{?user ns:estFilleDe ?parent} "
				+ "}";
				
		System.out.println("PARENTS : ");
		if(hasParents(nom))
			System.out.println(JenaEngine.executeQuery(inferedModel, query));
		else
			System.out.println(nom + "'s parents don't exist in the database!");

	}
	
	
	public static void afficherFreres(String nom)
	{
		String query = entete + "SELECT ?user ?sibling "
				+ "WHERE { "
				+ "{?user ns:estFrereDe ?sibling.} UNION {?user ns:estSoeurDe ?sibling.} "
				+ "?user ns:nom \"" + nom  + "\"."
				+ "}";
				
		System.out.println("SIBLINGS : ");
		
		if(hasSiblings(nom))
		   System.out.println(JenaEngine.executeQuery(inferedModel, query));
		else
			System.out.println(nom + " has no siblings !");
	}
	
	public static void afficherEnfants(String nom)
	{
		String query = entete + "SELECT ?user ?child "
				+ "WHERE { "
				+ "?user ns:estParentDe ?child. "
				+ "?user ns:nom \"" + nom  + "\"."
				+ "}";
				
		System.out.println("CHILDREN : ");
		
		if(hasChildren(nom))
		   System.out.println(JenaEngine.executeQuery(inferedModel, query));
		else
			System.out.println(nom + " has no children !");
	}
	
	public static void afficherMaris(String nom)
	{
		String query = entete + "SELECT ?user ?spouse "
				+ "WHERE { "
				+ "?user ns:se_marier_avec ?spouse. "
				+ "?user ns:nom \"" + nom  + "\"."
				+ "}";
				
		System.out.println("SPOUSES : ");
		if(isMarried(nom))
		{
			System.out.println(JenaEngine.executeQuery(inferedModel, query));
			
		}else
		{
			System.out.println(nom + " is not married");
		}
		
	}

	public static boolean isMarried(String nom)
	{
		String qu = entete + " ASK { "
    			+"?user ns:se_marier_avec ?spouse."
    			+"?user ns:nom \"" + nom + "\"."
				+"}";
        Query query = QueryFactory.create(qu) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, inferedModel) ;
        
        return qexec.execAsk();
	}
	
	public static boolean exists(String nom)
	{
		String qu = entete + " ASK { "
    			+"{?user rdf:type ns:Homme}UNION{?user rdf:type ns:Femme}?user ns:nom \""+nom+"\"."
				+"}";
        Query query = QueryFactory.create(qu) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, inferedModel) ;
        
        return qexec.execAsk();
	}
	
	public static boolean hasSiblings(String nom)
	{
		String qu = entete + " ASK { "
    			+"{?user ns:estFrereDe ?sibling.}UNION{?user ns:estSoeurDe ?sibling.}?user ns:nom \""+nom+"\"."
				+"}";
        Query query = QueryFactory.create(qu) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, inferedModel) ;
        
        return qexec.execAsk();
	}
	
	public static boolean hasChildren(String nom)
	{
		String qu = entete + " ASK { "
    			+"?user ns:estParentDe ?child.?user ns:nom \""+nom+"\"."
				+"}";
        Query query = QueryFactory.create(qu) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, inferedModel) ;
        
        return qexec.execAsk();
	}
	
	public static boolean hasParents(String nom)
	{
		String qu = entete + " ASK { "
    			+"?user ns:estEnfantDe ?parents. ?user ns:nom \"" + nom + "\"."
				+"}";
        Query query = QueryFactory.create(qu) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, inferedModel) ;
        
        return qexec.execAsk();
	}
	
	
	
}
