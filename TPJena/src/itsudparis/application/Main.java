/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itsudparis.application;


import com.hp.hpl.jena.rdf.model.Model;
import itsudparis.tools.JenaEngine;

/**
 *
 * @author DO.ITSUDPARIS
 */
public class Main {
    public static final String ns = "http://www.semanticweb.org/abdelhadikawtar/ontologies/2016/10/untitled-ontology-3#";
    public static String inputDataOntology = "data/FamilleTP2.owl";
    public static String inputRule = "data/rules.txt";
    public static String inputQuery = "data/femmes_plus_30.txt";

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args){
        Model model = JenaEngine.readModel(inputDataOntology);
        Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, inputRule);
        //query on the model
       System.out.println(JenaEngine.executeQueryFile(inferedModel, inputQuery));
        
    }

}
