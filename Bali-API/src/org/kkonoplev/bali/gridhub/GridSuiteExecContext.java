package org.kkonoplev.bali.gridhub;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.classifyreport.model.ClassifyReport;
import org.kkonoplev.bali.suiteexec.SuiteExecContext;
import org.kkonoplev.bali.suiteexec.TestExecContext;

public class GridSuiteExecContext implements Serializable{

	private static final Logger log = Logger.getLogger(GridSuiteExecContext.class);

	public static final String SUITE = "gridSuiteExecContext.txt"; 
	private GridSuiteMdl gsuiteMdl;
	private String resultDir;
	private Date startDate;
	private Date endDate;
	
	private ClassifyReport classifyReport;
	
	public GridSuiteMdl getGsuiteMdl() {
		return gsuiteMdl;
	}
	public void setGsuiteMdl(GridSuiteMdl gsuiteMdl) {
		this.gsuiteMdl = gsuiteMdl;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	
	public ClassifyReport getClassifyReport() {
		return classifyReport;
	}
	
	public void setClassifyReport(ClassifyReport classifyReport) {
		this.classifyReport = classifyReport;
	}
	
	public void serialize(File file){
		
		try {
			//log.info(toString()+" serializing to file "+file.getAbsolutePath());
		
			OutputStream fr = new BufferedOutputStream(new FileOutputStream(file));
			ObjectOutputStream oos = new ObjectOutputStream(fr);
			oos.writeObject(this);
			oos.flush();
			oos.close();
		} catch (Throwable t){
			log.warn("Error when serialing to file"+file.getAbsolutePath());
			log.warn(t,t);
		}
		
		//log.info(toString()+" completed serializing to file "+file.getAbsolutePath());		
		
	}
	
	public static GridSuiteExecContext deserialize(String string) throws IOException, ClassNotFoundException{
		
		//log.info("Deserialize GridSuiteExecContext from file: "+string);
		InputStream is = new BufferedInputStream(new FileInputStream(string));
		ObjectInputStream ois = new ObjectInputStream(is);
		GridSuiteExecContext suiteExecContext = (GridSuiteExecContext)ois.readObject();
		
		//log.info(suiteExecContext+" was successfully deserialized. ");

		return suiteExecContext;		
	}
	
}
