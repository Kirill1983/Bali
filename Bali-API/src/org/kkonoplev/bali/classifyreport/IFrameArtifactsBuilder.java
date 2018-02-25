package org.kkonoplev.bali.classifyreport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseArtifact;
import org.kkonoplev.bali.classifyreport.model.artifact.WarningCaseIFrameArtifact;
import org.kkonoplev.bali.suiteexec.TestExecContext;

public class IFrameArtifactsBuilder implements WarningCaseArtifactsBuilder {
	
	private static final Logger log = LogManager.getLogger(IFrameArtifactsBuilder.class);
	private File dir;
	private String file = "";
	private String addPathWeb = "";
	private String addPath = "";
	private String content = "";
	
	public IFrameArtifactsBuilder(String content, TestExecContext testExecContext){
		this.content = content;
				
		File reportDir = testExecContext.getSuiteExecContext().getResultDirFile();
		if (testExecContext.getClassName().lastIndexOf(".") != -1){
			addPath = testExecContext.getClassName().substring(0, testExecContext.getClassName().lastIndexOf(".")).replaceAll("\\.", "\\" + File.separator);
			addPathWeb = testExecContext.getClassName().substring(0, testExecContext.getClassName().lastIndexOf(".")).replaceAll("\\.", "/");
		}
		
		dir = new File(reportDir, addPath);

		// if run from Eclipse need to create dir self
		if (!dir.exists())
			dir.mkdirs();

		long time = System.currentTimeMillis();
		file = "frame-"+testExecContext.getRunnableNode().getName()+"-"+testExecContext.getThreadId()+"-"+time+".html";
	}

	@Override
	public List<WarningCaseArtifact> build() {
		savefile(content, dir, file);
	
		List<WarningCaseArtifact> list = new ArrayList<WarningCaseArtifact>();
		
		String filePath = file;
		if (!addPathWeb.equals(""))
			filePath = addPathWeb+"/"+file;
		
		list.add(new WarningCaseIFrameArtifact(filePath));
		
		return list;
	}

	private void savefile(String content, File packageDir, String file) {
		
		try {
			packageDir.mkdirs();
			File htmlFile = new File(packageDir, file);
			OutputStream out = new FileOutputStream(htmlFile);
			out.write(content.getBytes());
			out.close();
		} catch (Exception e) {
			log.warn("Can't save iframe file "+file, e);
		}
		
	}
	

	
	

}
