package org.kkonoplev.bali.runner.cucumber;



import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CukeReporter implements Formatter, Reporter {

	public static Logger log  = Logger.getLogger("org.kkonoplev.bali.CukeReporter");
	
    private final List<Step> steps = new ArrayList<Step>();
    private boolean ignoredStep;
    private boolean inScenarioLifeCycle;

    private int scenariousCycle = 0;
    private int examplesSize = 0;

    private Feature feature;
    private Background background;
    private ScenarioOutline scenarioOutline;
    private Scenario scenario;
    private Examples examples;
    private int currentExamplesCase = 1;
    private boolean itemRunned = false;

    public CukeReporter(){
    }


    @Override
    public void uri(String uri) {
        System.out.println("URI : "+uri.toString());
    }

    @Override
    public void feature(Feature feature) {
        this.feature = feature;
        System.out.println("FEATURE: "+feature.getName());
    }


    @Override
    public void background(Background background) {
        this.background = background;

        System.out.println();
        System.out.println("BACKGROUND " + background.getName());
    }

    @Override
    public void scenario(Scenario scenario) {
    	

        this.scenario = scenario;
        System.out.println();
        System.out.println("SCENARIO "+scenario.getName());
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {

        this.scenarioOutline = scenarioOutline;
        System.out.println();
        System.out.println("SCENARIO OUTLINE " + scenarioOutline.getName());
    }

    @Override
    public void examples(Examples examples) {

        currentExamplesCase = 1;
        examplesSize = examples.getRows().size()-1;
        this.examples = examples;

        System.out.println();
        System.out.println("EXAMPLES NAMES:" + examples.getName());
        
        for (ExamplesTableRow examplesTableRow: examples.getRows()){

            System.out.println();
            for (String c: examplesTableRow.getCells()){
                System.out.print(c+" ");
            }

        }

    }

    @Override
    public void step(Step step) {

    	itemRunned = true;

        if (steps != null)
            steps.add(step);

        System.out.println();
        //log.info("STEP "+step.getName());

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
    	
    	if (!itemRunned)
    		throw new RuntimeException("There was no scenario with steps identified! Please check!");
    	
        System.out.println("DONE");
    }

    @Override
    public void close() {
        System.out.println("CLOSE");
    }

    public void startOfScenarioLifeCycle(Scenario scenario) {

        steps.clear();;

        scenariousCycle++;
        System.out.println("");
        System.out.println("<<<<<<<<<< "+(scenariousCycle));
        System.out.println("START OF SCENARIO LIFECYCLE: "+scenario.getName());
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {
        System.out.println("END OF SCENARIO LIFECYCLE: " + scenario.getName());
        System.out.println(">>>>>>>>>>> "+(scenariousCycle));
        currentExamplesCase++;

        // means end of scenario outline
        // next will be scenario outline or scenario
        if (currentExamplesCase > examplesSize){
            System.out.println("reset scenario outline");
            this.scenario = null;
            this.scenarioOutline = null;
            examples = null;
            currentExamplesCase = 1;
            examplesSize = 0;
        }
    }

    public void before(Match match, Result result) {
    }

    @Override
    public void result(Result result) {

        System.out.println();
        boolean failed = result.getStatus().equals("failed");
        if (failed) {
        	
            BaliTestContext.addError(result);
            System.out.println("Result error msg="+result.getErrorMessage()+", status = "+result.getStatus()+", duration = "+result.getDuration());

            System.out.println("<------ERROR INFO START--------->");

            System.out.println("FEATURE:"+feature.getName());

            if (scenarioOutline != null)
                System.out.println("SCENARIO OUTLINE:"+scenarioOutline.getName());

            if (scenario != null)
                System.out.println("SCENARIO:"+scenario.getName());

            System.out.println("");

            /*
            int i  = 1;
            for (Step step: steps){
            	log.warn("step "+(i++)+"."+ step.getName());
            }

            log.warn("examples case/all:" + currentExamplesCase+"/"+examplesSize);

            if (examples != null) {
            	log.warn(examples.getRows().get(0).getCells().toString());
                log.warn(examples.getRows().get(currentExamplesCase).getCells().toString());
            }
		
            System.out.println("<------ERROR INFO END----------->");
            */

        }


    }

    public void after(Match match, Result result) {
    }

    @Override
    public void match(Match match) {
        //System.out.println("Match "+match.getLocation());
    }

    public void embedding(String mimeType, byte[] data) {

    }

    @Override
    public void write(String text) {

    }


	@Override
	public void embedding(String arg0, InputStream arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2,
			String arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}
}
