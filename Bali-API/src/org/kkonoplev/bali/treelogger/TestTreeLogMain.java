package org.kkonoplev.bali.treelogger;

public class TestTreeLogMain {

	public static void main(String[] args) {
		
		TreeLog tlog = new TreeLog();
		
		tlog.openLevel("Open smarterselect site");
		tlog.log("prod -> app.smarterselect.com");
		tlog.closeLevel();
		
		tlog.openLevel("Apply program");
			tlog.openLevel("open app apply url");
			tlog.log("url -> www.ss.com/123/apply");
			tlog.closeLevel();

			tlog.openLevel("Create new program ");
			tlog.log("click #new");
			tlog.closeLevel();
		tlog.closeLevel();

		tlog.openLevel("Fill form");
	
		tlog.openLevel("input name");
		tlog.log("find field #a");
		tlog.log("input in #a value name");
		tlog.closeLevel();
		
		tlog.openLevel("input second name");
		tlog.log("find field #a");
		tlog.log("input in #a value name");
		tlog.closeLevel();

		tlog.openLevel("Click submit");
		tlog.log("find field #submit");
		tlog.log("click");
		tlog.closeLevel();

		tlog.closeLevel();
		
		tlog.openLevel("Assert form");
		
		tlog.openLevel("check name field");
		tlog.logFail("find field #a");
		tlog.log("check value in #a name");
		tlog.closeLevel();
		
		tlog.openLevel("check second name");
		tlog.log("find field #a");
		tlog.logFail("check value in #a value name");
		tlog.closeLevel();

		tlog.closeLevel();
		
		String fname = "treeLog.html";
		TreeLogHTMLBuilder builder = new TreeLogHTMLBuilder(tlog);
		builder.saveFile(fname);
		
	}

}
