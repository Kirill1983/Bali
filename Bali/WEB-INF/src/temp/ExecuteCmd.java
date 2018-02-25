
package temp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ExecuteCmd extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int count = 0;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        PrintWriter page = res.getWriter();
        res.setCharacterEncoding("UTF-8");
        page.println("typeperf \"\\Process(firefox)\\% Processor Time");

        
        String command = "typeperf \"\\Process(firefox)\\% Processor Time";
        Process proc = Runtime.getRuntime().exec(command, null);
        
        
        
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);

            try {
                String line;
                int i = 0;
                while ((line = in.readLine()) != null) {
                    page.println("line "+i+": "+line);
                    i++;
                    if (i > 50)
                        break;
                }
                Thread.sleep(50);

                //proc.waitFor();
                System.out.println("in.close()");
                in.close();

                System.out.println("out.close()");
                out.close();

                System.out.println("proc.destroy()");
                proc.destroy();

                System.out.println("command launched.");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }




    }



}
