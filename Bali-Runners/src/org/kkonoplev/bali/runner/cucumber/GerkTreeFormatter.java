package org.kkonoplev.bali.runner.cucumber;


import gherkin.formatter.Formatter;
import gherkin.formatter.model.*;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.suiteexec.resource.EnvResource;
import org.kkonoplev.bali.suiteexec.resource.RequiredTestResource;

import java.util.ArrayList;
import java.util.List;

public class GerkTreeFormatter implements Formatter {


    private TreeNode featureNode;

    private final List<Step> steps = new ArrayList<Step>();
    private boolean ignoredStep;
    private boolean inScenarioLifeCycle;

    private int scenariousCycle = 0;

    private Feature feature;
    private Background background;
    private ScenarioOutline scenarioOutline;
    private Scenario scenario;
    private Examples examples;
    private int currentExamplesCase = 1;
    private String featureFile;
    private String featureFileShort;
    
    private int scenariousCase = 1;
    private int outlineCase = 1;
    private int scenarioCase = 1;

    private TreeNode scenarioOutlineNode;
    private TreeNode lastStepsNode;    

    private CucumberProject project;

    public GerkTreeFormatter(BaseProject project){
    	this.project = (CucumberProject)project;
        featureNode = new TreeNode();
        featureNode.setColor("2915c4");
    }


    @Override
    public void uri(String uri) {
        System.out.println("URI : "+uri);
        featureFile  = uri;
        featureFileShort = getShortName(featureFile);
     
    }
    
    public String getShortName(String absPath){
    	
    	String file = absPath;
    			
    	int indx = absPath.lastIndexOf("\\");
    	if (indx != -1)
    		file = absPath.substring(indx+1);
    	
    	int pointIndx = file.lastIndexOf(".");
    	String name = file;
    	
    	if (pointIndx != -1)
    		name = file.substring(0, pointIndx);
    	
    	return name;   	    	
    }

    @Override
    public void feature(Feature feature) {
        this.feature = feature;
        System.out.println("FEATURE: "+feature.getName());
        featureNode.setName(featureFileShort);
        featureNode.setLabel(featureFileShort);
        
        
        String tags = "";
        for (Tag tag: feature.getTags()){
           tags += tag.getName()+" ";
        }
        System.out.println("TAGS: "+tags);
        
        featureNode.setTitle(correctHtmlText("FEATURE: "+feature.getName()+"\n"+featureFile+"\nTAGS: "+tags));
        featureNode.setTags(convertTags(feature.getTags()));

    }
    
    public String correctHtmlText(String htmlText){
    	String txt = htmlText.replaceAll("'", "");
    	return txt;
    }
    
    public List<String> convertTags(List<Tag> tags){

    	List<String> list = new ArrayList<String>();
        for (Tag tag: tags){
            list.add(tag.getName());
        }
        return list;
        
    }

    public String shorten(String txt){
    	
    	
    	int size = 50;
    	if (txt.length() <= size)
    		return txt;
    		
    	String a = txt;
    	
    	String p1 = a.substring(0, size)+"..";
    	return p1;
    	
    }

    @Override
    public void background(Background background) {
        this.background = background;

        System.out.println();
        System.out.println("BACKGROUND " + background.getName());
    }

    public void addStepsDescToLastScenarioNode(){
    	
        if (lastStepsNode != null) {
            String initTitle = lastStepsNode.getTitle();
            String stepsText = this.getStepsText();
            lastStepsNode.setTitle(correctHtmlText(initTitle+stepsText));
            steps.clear();
        }


    }
    @Override
    public void scenario(Scenario scenario) {

        if (scenarioOutlineNode != null){
            featureNode.getChilds().add(scenarioOutlineNode);
            scenarioOutlineNode = null;
        }
        
        this.scenario = scenario;
        System.out.println();
        System.out.println("SCENARIO "+scenario.getName());

        addStepsDescToLastScenarioNode();
        
        String tags = "";
        for (Tag tag: scenario.getTags()){
            tags += tag.getName()+" ";
        }
        System.out.println("TAGS: "+tags);

        TreeNode scenarioNode = new TreeNode();
        scenarioNode.setTitle("TAGS=" + tags + "\nSCENARIO " + scenario.getName());
        scenarioNode.setLabel(shorten(scenario.getName()));        scenarioNode.setName("scenario"+scenarioCase);
        scenarioNode.setColor("514e66");
        scenarioNode.setLineViewLabel(featureFileShort + " -> " +scenario.getName());
        
        scenarioNode.setTags(convertTags(scenario.getTags()));
        addNewTags(scenarioNode.getTags(), featureNode.getTags());
        scenarioNode.setTitle("TAGS=" + scenarioNode.getTags() + "\nSCENARIO " + scenario.getName());
        
        CucumberRunnableItem item = new CucumberRunnableItem();
        item.setCucumberProject(project);
        item.setFeatureFile(featureFile);
        item.setStepsDir(project.getStepsDir());
        item.setScenarioName(scenario.getName());
        item.setRequiredTestResources(parseRequiredResources(scenarioNode.getTags()));
        scenarioNode.setRunnableItem(item);
        
        featureNode.getChilds().add(scenarioNode);
        lastStepsNode = scenarioNode;
        scenarioCase++;        
        
        project.setTestcount(project.getTestcount()+1); 


    }



	public TreeNode getFeatureNode() {
        return featureNode;
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {

        if (scenarioOutlineNode != null)
            featureNode.getChilds().add(scenarioOutlineNode);
        
        addStepsDescToLastScenarioNode();

        this.scenarioOutline = scenarioOutline;
        scenariousCase = 1;
        System.out.println();
        System.out.println("SCENARIO OUTLINE " + scenarioOutline.getName());

        scenarioOutlineNode = new TreeNode();
        scenarioOutlineNode.setTitle("SCENARIO OUTLINE " + scenarioOutline.getName());
        scenarioOutlineNode.setLabel(shorten(scenarioOutline.getName()));
        scenarioOutlineNode.setName("outline"+outlineCase);
        scenarioOutlineNode.setColor("7356c9");
        scenarioOutlineNode.setTags(convertTags(scenarioOutline.getTags()));
        addNewTags(scenarioOutlineNode.getTags(), featureNode.getTags());
        scenarioOutlineNode.setTitle("Tags: "+scenarioOutlineNode.getTags()+"\nSCENARIO OUTLINE " + scenarioOutline.getName());
        
        lastStepsNode = scenarioOutlineNode;
        
        outlineCase++;

    }

    private void addNewTags(List<String> tags, List<String> tagsExtra) {
		
    	for (String tag: tagsExtra){
    		if (!tags.contains(tag))
    			tags.add(tag);
    	}
		
	}


	@Override
    public void examples(Examples examples) {

        currentExamplesCase = 1;
        this.examples = examples;

        String tags = "";
        for (Tag tag: examples.getTags()){
            tags += tag.getName()+" ";
        }

        String dataRows = "";
        for (ExamplesTableRow examplesTableRow: examples.getRows()){
            for (String cell: examplesTableRow.getCells()){
            	dataRows += cell+" | ";
            }
            dataRows += "\n";
        }
        
        project.setTestcount(project.getTestcount()+examples.getRows().size()-1); 
        
        System.out.println(dataRows);

        System.out.println("    SCENARIOUS:" + examples.getName());
        System.out.println("    TAGS: "+tags);
        
        TreeNode scenariousNode = new TreeNode();
        
        String label = examples.getName();
        if (label.equals(""))
        	label = "examples";
        
        scenariousNode.setLabel(shorten(label));
        scenariousNode.setName("scenarious"+scenariousCase);
        scenariousNode.setColor("514e66");
        
        scenariousNode.setTags(convertTags(examples.getTags()));
        addNewTags(scenariousNode.getTags(), scenarioOutlineNode.getTags());
        scenariousNode.setTitle("TAGS: "+scenariousNode.getTags().toString()+"\nSCENARIOUS:"+examples.getName()+"\nExamples:\n"+dataRows);
        
        scenariousNode.setLineViewLabel(featureFileShort + " -> " +scenarioOutline.getName() + " -> "+examples.getName());
        
        CucumberRunnableItem item = new CucumberRunnableItem();
        item.setCucumberProject(project);
        item.setFeatureFile(featureFile);
        item.setStepsDir(project.getStepsDir());
        item.setScenarioName(examples.getName());
        item.setRequiredTestResources(parseRequiredResources(scenariousNode.getTags()));
        
        scenariousNode.setRunnableItem(item);
        scenarioOutlineNode.getChilds().add(scenariousNode);
        scenariousCase++;
        
        
                

    }
	
    private List<RequiredTestResource> parseRequiredResources(List<String> tags) {
    	
    	List<RequiredTestResource> resources = new ArrayList<RequiredTestResource>();
    	for (String tag: tags){
    		
    		if (tag.endsWith(EnvResource.ENVRESOURCE)){
    			String shortTag = tag.substring(0, tag.length()-EnvResource.ENVRESOURCE.length()); 
    			RequiredTestResource requiredTestResource = new RequiredTestResource(EnvResource.class, shortTag);
    			resources.add(requiredTestResource);
    		}
    		else
    		if (tag.endsWith(EnvResource.RESOURCE)){
				String shortTag = tag.substring(0, tag.length()-EnvResource.RESOURCE.length());
				RequiredTestResource requiredTestResource = new RequiredTestResource(EnvResource.class, shortTag+" "+EnvResource.ALLENV);
				resources.add(requiredTestResource);
    		}
    		
    	}
    	
		return resources;
	}


    @Override
    public void step(Step step) {
    	steps.add(step);
    }
    
    public String getStepsText(){

        String stepTxt = "\n\nSTEPS\n";
        for (Step step: steps){
            stepTxt += step.getKeyword()+" "+step.getName()+ "\n";
        }
        return stepTxt;        

    }


    @Override
    public void eof() {

        System.out.println("EOF");
    }

    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
        System.out.println("Syntax error " + state);
    }

    @Override
    public void done() {

    	// if lastStepNode is scenarioOutline -> add it!
    	if (lastStepsNode.getChilds().size() > 0){
    		addStepsDescToLastScenarioNode();
            featureNode.getChilds().add(scenarioOutlineNode);
    	}
    	
        System.out.println("DONE");
    }

    @Override
    public void close() {

    	addStepsDescToLastScenarioNode();
        System.out.println("CLOSE");
    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {
    }


	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2,
			String arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}


}
