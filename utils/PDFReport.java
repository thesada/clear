package clear.utils;

import java.io.FileInputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

 
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;
 
import clear.driver.TestDriver;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Anchor;
//-------------PDF imports-------------
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
* This class implementation of IReporter, to customize the reporting
* @author ptt4kor
* @version 1.0
*/
 
@SuppressWarnings("unused")
public class PDFReport extends TestListenerAdapter implements IReporter
{
    
    
	private static int anchorLinkCount = 0;
    private static int anchorTargetCount = 0;
   
   
    
    private static Font sagoe8;
    private static Font sagoe8Bold;
    private static Font sagoe8Link ;
    private static Font sagoe10Bold;
    private static Font sagoe10;
    private static Font sagoe12Bold ;
    private static Font sagoe12;
    private static Font sagoe17Bold;
    private static Font sagoe16Bold;
    private static Font sagoe14Bold;
    private static Font sagoe16Symbol;
    private static Font sagoe12Label;
    private static Font sagoe12value;
    
    private static BaseColor testTableclr = new BaseColor(196, 211, 226);
    private static BaseColor passColor = new BaseColor(164,196,0);
    private static BaseColor failColor = new BaseColor(238,92,66);
    private static BaseColor ucColor = new BaseColor(170,170,170);
    
    Properties settings = null;
    Document pdf = null;
    

    public void generateReport(List<XmlSuite> arg0, List<ISuite> suites, String outdir)
    {
    	pdf = new Document( PageSize.A4);
    	pdf.setMargins(0, 0, 8	, 0);
    	 
    	 FontFactory.register("C:/Windows/Fonts/segoeui.TTF", "Segoe UI Symbol");
    	
    	FontFactory.getRegisteredFonts();
    	
		sagoe8 = FontFactory.getFont("Segoe UI Symbol", 9, Font.NORMAL);
		sagoe8Bold = FontFactory.getFont("Segoe UI Symbol", 8, Font.BOLD);
		sagoe8Link = FontFactory.getFont("Segoe UI Symbol", 9, Font.NORMAL,BaseColor.BLUE);
		sagoe10Bold = FontFactory.getFont("Segoe UI Symbol", 10, Font.BOLD);
		sagoe10 = FontFactory.getFont("Segoe UI Symbol", 10, Font.NORMAL);
		sagoe12Bold = FontFactory.getFont("Segoe UI Symbol", 12, Font.BOLD);
		sagoe12 = FontFactory.getFont("Segoe UI Symbol", 12, Font.NORMAL);
		sagoe17Bold = FontFactory.getFont("Segoe UI Symbol", 17, Font.BOLD,BaseColor.WHITE);
		sagoe16Bold = FontFactory.getFont("Segoe UI Symbol", 16, Font.BOLD,BaseColor.LIGHT_GRAY);
		sagoe14Bold = FontFactory.getFont("Segoe UI Symbol", 14, Font.BOLD);
		sagoe16Symbol = FontFactory.getFont("Segoe UI Symbol", 14, Font.BOLD);
		sagoe12Label = FontFactory.getFont("Segoe UI Symbol", 10, Font.BOLD,BaseColor.LIGHT_GRAY);
		sagoe12value = FontFactory.getFont("Segoe UI Symbol", 10, Font.NORMAL,BaseColor.LIGHT_GRAY);
 
        try {
        	
        	FileInputStream fip;
    		// load config.properties
        	settings = new Properties();
    		//TODO: make it dynamic
        	
    		fip = new FileInputStream(TestDriver.configPath + "/config.properties");
    		settings.load(fip);
    		
    		//create pdf file name
    		 
            PdfWriter.getInstance(pdf,new FileOutputStream(settings.getProperty("REPORT_PDF") + "TestReport_" + (new SimpleDateFormat("ddMMMyyHHmma").format(new Date())) + ".pdf"));
        	//PdfWriter.getInstance(pdf,new FileOutputStream("TestReport.pdf"));
            pdf.open();
            
            //Set footer
            
            /*Header header = new Header(new Phrase(chunk), false);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setBorder(Rectangle.NO_BORDER);
            document.setHeader(header);*/
            //bosch logo
 			
 			/*Image logoImg = Image.getInstance("resources//images//pdf//" + "logo.jpg");
 			logoImg.scaleAbsolute(80f, 35f);
 			logoImg.setAlignment(Image.ALIGN_CENTER);
 			logoImg.setAlignment(Image.ALIGN_MIDDLE);
 			
 			pc = new PdfPCell(logoImg);
 			pc.setBorder(Rectangle.NO_BORDER);
 			pc.setHorizontalAlignment(Element.ALIGN_CENTER);
 			pc.setBackgroundColor(new BaseColor(47, 94, 141));
 			pc.setRowspan(3);
 			headerTable.addCell(pc);*/
          	
            
            //add meta data
            addMetaData();
            	
	        //add title page
            addHeader();
 
            //add summary
            addTestSummary(suites);
            
            //add summary
            addDetailsReport(suites);
           
            pdf.close(); // no need to close PDFwriter?
 
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
   }
 
    private  void addMetaData() {
        pdf.addTitle("Automation Results");
        pdf.addSubject("Automation results");
        pdf.addKeywords("Verification, PAT");
        pdf.addAuthor("ptt4kor");
        pdf.addCreator("ptt4kor");
      }
 
    private  void addHeader() throws DocumentException, IOException {
          Paragraph paragraph = new Paragraph();
          
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm");
          Date date = new Date();
          String dateNow = sdf.format(date); 
          
          	//Header first line
          	PdfPTable headerTable = new PdfPTable(1);
          	headerTable.setWidthPercentage(100);
          	
          	headerTable.setSpacingAfter(25);
          	
          	PdfPCell pc = new PdfPCell(new Phrase(" ",sagoe8));
 			pc.setBorder(Rectangle.NO_BORDER);
 			pc.setIndent(25);
 			pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
 			pc.setBackgroundColor(new BaseColor(47, 94, 141));
 			headerTable.addCell(pc);
 			
 			
 			//header first line
 	 		pc = new PdfPCell(new Phrase("Automation Test Execution Report",sagoe17Bold));
 	 		
 			pc.setBorder(Rectangle.NO_BORDER);
 			pc.setIndent(25);
 			pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
 			pc.setBackgroundColor(new BaseColor(47, 94, 141));
 			headerTable.addCell(pc);
 			
 			
          
 			//Header second line
 			   Chunk ck = new Chunk("Date: ",sagoe12Label);
               paragraph.add(ck);
               ck = new Chunk(dateNow,sagoe12value);
               paragraph.add(ck);
               
               
               ck = new Chunk("      Build#: ",sagoe12Label);
               paragraph.add(ck);
               ck = new Chunk(TestDriver.buildNumber,sagoe12value);
               paragraph.add(ck);
               
               
               ck = new Chunk("      Tester: ",sagoe12Label);
               paragraph.add(ck);
               ck = new Chunk(TestDriver.tester,sagoe12value);
               paragraph.add(ck);
               
               addEmptyLine(paragraph,1);
               
              
                pc = new PdfPCell(paragraph);
    			pc.setBorder(Rectangle.NO_BORDER);
    			pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
    			pc.setIndent(25);
    			pc.setColspan(2);
    			pc.setBackgroundColor(new BaseColor(47, 94, 141));
    			headerTable.addCell(pc);
    			
    			pdf.add(headerTable);
    			
    		
      }
 
 
 
    private  void addTestSummary(List<ISuite> suites) throws DocumentException {
          Paragraph para = null;
          PdfPTable summaryTable = new PdfPTable(new float[]{.8f, .6f, .2f, .2f, .2f, .3f, .2f});
        	summaryTable.setWidthPercentage(95);
        	BaseColor summaryTabHeader = new BaseColor(196, 211, 226);
        	 
                PdfPCell pc = new PdfPCell(new Phrase("Summary",sagoe14Bold));
        		pc.setColspan(7);
        		pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        		pc.setBorder(Rectangle.BOTTOM);
	           pc.setBackgroundColor(new BaseColor(125, 158, 192));
	           summaryTable.addCell(pc);
                
                
 
                 pc = new PdfPCell(new Phrase("Test Name",sagoe10Bold));
                pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                pc.setBackgroundColor(summaryTabHeader);
                summaryTable.addCell(pc);
               
                pc = new PdfPCell(new Phrase("Objective",sagoe10Bold));
                pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                pc.setBackgroundColor(summaryTabHeader);
                summaryTable.addCell(pc);
                
                pc = new PdfPCell(new Phrase("Result",sagoe10Bold));
                pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                pc.setBackgroundColor(summaryTabHeader);
                summaryTable.addCell(pc);
       
		          
                	//Create steps table
                	PdfPTable stepsTable = new PdfPTable(new float[]{ .2f, .2f, .3f});
	               	
                		pc = new PdfPCell(new Phrase("Steps",sagoe10Bold));
                		pc.setColspan(3);
                		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
			           pc.setBackgroundColor(summaryTabHeader);
	                   stepsTable.addCell(pc);
	                   
	                   pc = new PdfPCell(new Phrase("Passed",sagoe10Bold));
			           pc.setHorizontalAlignment(Element.ALIGN_LEFT);
			           pc.setBackgroundColor(summaryTabHeader);
			           stepsTable.addCell(pc);
			           
			           pc = new PdfPCell(new Phrase("Failed",sagoe10Bold));
			           pc.setHorizontalAlignment(Element.ALIGN_LEFT);
			           pc.setBackgroundColor(summaryTabHeader);
			           stepsTable.addCell(pc);
			           
			           pc = new PdfPCell(new Phrase("Warnings", sagoe10Bold));
			           pc.setHorizontalAlignment(Element.ALIGN_LEFT);
			           pc.setBackgroundColor(summaryTabHeader);
			           stepsTable.addCell(pc);
			           
			           //Add steps table to summary table
			           pc = new PdfPCell(stepsTable);
	                   pc.setColspan(3);
	                   summaryTable.addCell(pc);
			         //End step table
                	
		          
		           pc = new PdfPCell(new Phrase("Time      (in sec)",sagoe10Bold));
		           pc.setHorizontalAlignment(Element.ALIGN_LEFT);
		           pc.setBackgroundColor(summaryTabHeader);
		           summaryTable.addCell(pc);
		          
		           summaryTable.setHeaderRows(1);
 
			for (ISuite suite : suites) {
				Map<String, ISuiteResult> r = suite.getResults();
				for (ISuiteResult r2 : r.values()) {
					ITestContext testContext = r2.getTestContext();

					if (r.values().size() > 0) {
						// m_out.println("<h1>" + testContext.getName() +
						// "</h1>");
					}
					
					printSummary(summaryTable, testContext.getPassedTests(),"Passed");
					printSummary(summaryTable, testContext.getFailedTests(),"Failed");
					//CHECK CHECK
					/*for (ITestResult dum : testContext.getFailedConfigurations().getAllResults()) {
						Object[] o = dum.getParameters();
						Reporter.getOutput(dum);
						dum.getParameters();
					}*/
					//END CHECK
					
					
					}
				}

				pdf.add(summaryTable);
         	
      }
    
    
     
    private void printSummary(PdfPTable summaryTable, IResultMap tests, final String style){
    	
    	int passedCount,failedCount,warningsCount,ucCount = 0;
    	
    	//Sort the results based on Test case name
    	 List<ITestResult> sortRes = new ArrayList<ITestResult>(tests.getAllResults());
         Collections.sort(sortRes,new TestSorter() );
       
        //end sorting
    	for (ITestResult result : sortRes) {
    		
            ITestNGMethod method = result.getMethod();
             //String methodName = method.getDescription();
            //String methodName = result.getInstance().toString();
            String cname = method.getTestClass().getName();
            String[] temp = cname.split("\\.");
            cname = temp[temp.length-1];
            
            //print test case id/ class name
            Anchor anchor = new Anchor(cname, sagoe8Link);
            anchor.setReference("#linkTarget" + anchorLinkCount++);
            
            PdfPCell pc = new PdfPCell(anchor);
            pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            pc.setFixedHeight(15);
            summaryTable.addCell(pc);
            
            //print objective
            Object[] params = result.getParameters();
            
            
            if(params.length > 0){
            	@SuppressWarnings("unchecked")
				HashMap<String,String> xlData =  (HashMap<String, String>) params[0];
            	pc = new PdfPCell(new Phrase(xlData.get("OBJECTIVE"),sagoe8));
            }
            else{
            	pc = new PdfPCell(new Phrase(cname,sagoe8));
            }
            
            pc.setFixedHeight(15);
            pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(pc);
            
            //print result
            passedCount = getPassedStepCount(result);
            failedCount = getFailedStepCount(result);
            warningsCount = getWarningsCount(result);
            ucCount =  getUncompletedCount(result);
            
            
            if((ucCount>0) || (passedCount==0 && failedCount == 0 )){
            	pc = new PdfPCell(new Phrase("NC",sagoe8));
            	pc.setBackgroundColor(ucColor);
            }
            else if(failedCount>0){
            	pc = new PdfPCell(new Phrase("Failed",sagoe8));
            	pc.setBackgroundColor(failColor);
            }else
            {
            	pc = new PdfPCell(new Phrase("Passed",sagoe8));
            	pc.setBackgroundColor(passColor);
            }
            
            pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            pc.setFixedHeight(15);
            summaryTable.addCell(pc);
            
            //print passed step count
            pc = new PdfPCell(new Phrase(String.valueOf(passedCount), sagoe8));
            pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            pc.setFixedHeight(15);
            summaryTable.addCell(pc);
            
            //print failed step count
            pc = new PdfPCell(new Phrase(String.valueOf(failedCount),sagoe8));
            pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            pc.setFixedHeight(15);
            summaryTable.addCell(pc);
            
            //print skipped step count
            pc = new PdfPCell(new Phrase(String.valueOf(warningsCount),sagoe8));
            pc.setFixedHeight(15);
            pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            summaryTable.addCell(pc);
            
            //print time taken
            
            pc = new PdfPCell(new Phrase(calculateTimeTaken(result),sagoe8));
            pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            pc.setFixedHeight(15);
            summaryTable.addCell(pc);
       
    	}
    }
    
    /**
	 * Method to calculate time take by method/script to execute
	 * @param result of ITestResult
	 * @throws  
	 * @author ptt4kor
	 * 
	 */
    private String calculateTimeTaken(ITestResult result){
    	//time taken calculations
    	String timeStr = "-";
        long end = result.getEndMillis();
        long start =result.getStartMillis();
        long timeTaken = (end - start)/1000;
        //timeStr = 
        return String.valueOf(timeTaken);
    	
    }
    private  void addDetailsReport(List<ISuite> suites) throws Exception {
    	
    	for (ISuite suite : suites) {
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();

				if (r.values().size() > 0) {
					// m_out.println("<h1>" + testContext.getName() +
					// "</h1>");
				}
				
				createTestCaseDetails(testContext.getPassedTests());
				createTestCaseDetails(testContext.getFailedTests());
				}
			}
    }
    
    private  void createTestCaseDetails(IResultMap tests) throws Exception{
    	
    	List<ITestResult> sortRes = new ArrayList<ITestResult>(tests.getAllResults());
        Collections.sort(sortRes,new TestSorter() );
    	
        //display
    	for (ITestResult result : sortRes) {
    		ITestNGMethod method = result.getMethod();
    		Paragraph headParam = new Paragraph();
    		headParam.setAlignment(Element.ALIGN_CENTER);
    		String cname = method.getTestClass().getName();
    	    String[] temp = cname.split("\\.");
    	     cname = temp[temp.length-1];
    		pdf.add(Chunk.NEXTPAGE);
    		 
    		//add ribbon to each page
    		PdfPTable headerTable = new PdfPTable(1);
           	headerTable.setWidthPercentage(100);
           	
           	headerTable.setSpacingAfter(10);
           	//---- link the page---
           	
           	
           	PdfPCell pc = new PdfPCell(new Phrase(" "));
  			pc.setBorder(Rectangle.NO_BORDER);
  			pc.setIndent(25);
  			pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
  			pc.setBackgroundColor(new BaseColor(47, 94, 141));
  			headerTable.addCell(pc);
           	
  			
  			Anchor anchorTarget = new Anchor("Detailed Test Steps Report: "+ cname,sagoe16Bold);
           	anchorTarget.setName("linkTarget" + anchorTargetCount++);
           	
  			pc = new PdfPCell(anchorTarget);
  			pc.setBorder(Rectangle.NO_BORDER);
  			pc.setIndent(25);
  			pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
  			pc.setBackgroundColor(new BaseColor(47, 94, 141));
  			pc.setFixedHeight(30);
  			headerTable.addCell(pc);
  			pdf.add(headerTable);
	        
	        createTestCaseDetailsTable(result);
	      
	        createParameterTable(result);
	        
	        createMessageTable(result);
	   }
    }
    
    private  void createTestCaseDetailsTable(ITestResult res) throws DocumentException{
    	int passedCount,failedCount,skippedCount, ucCount=0;
    	
    	ITestNGMethod method = res.getMethod();
    	
    	String cname = method.getTestClass().getName();
        String[] temp = cname.split("\\.");
        cname = temp[temp.length-1];
        
        
        
    	PdfPTable testTable = new PdfPTable(new float[]{.4f, .2f,.2f, .2f});
    	testTable.setWidthPercentage(80);
    	testTable.setSpacingBefore(4);
    	testTable.setHorizontalAlignment(Element.ALIGN_MIDDLE);
   	 	//Set table header....
    	PdfPCell pc = new PdfPCell(new Phrase("Test Details",sagoe14Bold));
        pc.setHorizontalAlignment(Element.ALIGN_CENTER);
        pc.setBackgroundColor(new BaseColor(125, 158, 192));
        pc.setBorder(Rectangle.BOTTOM);
        pc.setColspan(4);
        testTable.addCell(pc);
    	
    	pc = new PdfPCell(new Phrase("Test",sagoe10Bold));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBackgroundColor(testTableclr);
        pc.setBorder(Rectangle.NO_BORDER);
        testTable.addCell(pc);
        
       
        
        pc = new PdfPCell(new Phrase("Result",sagoe10Bold));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBackgroundColor(testTableclr);
        pc.setBorder(Rectangle.NO_BORDER);
        testTable.addCell(pc);
        
        pc = new PdfPCell(new Phrase("Success%",sagoe10Bold));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBackgroundColor(testTableclr);
        pc.setBorder(Rectangle.NO_BORDER);
        testTable.addCell(pc);
    	
        pc = new PdfPCell(new Phrase("Time Taken(sec)",sagoe10Bold));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBackgroundColor(testTableclr);
        pc.setBorder(Rectangle.NO_BORDER);
        testTable.addCell(pc);
        
       //Set values
        //--test
        pc = new PdfPCell(new Phrase(cname, sagoe10));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBorder(Rectangle.NO_BORDER);
        pc.setFixedHeight(18);
        testTable.addCell(pc);
        
   
        //--Test Result
        Chunk chunkPassFail= null;
        passedCount = getPassedStepCount(res);
        failedCount = getFailedStepCount(res);
        skippedCount = 0;
        ucCount =  getUncompletedCount(res);
        
        
        if((ucCount>0) || (passedCount==0 && failedCount == 0 )){
        	chunkPassFail = new Chunk("Not Completed",sagoe10);
        	chunkPassFail.setBackground(ucColor);
        }
        else if(failedCount>0){
        	chunkPassFail = new Chunk("Failed",sagoe10);
        	chunkPassFail.setBackground(failColor);
        }else
        {
        	chunkPassFail = new Chunk("Passed",sagoe10);
        	chunkPassFail.setBackground(passColor);
        }
        
        pc = new PdfPCell(new Phrase(chunkPassFail));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBorder(Rectangle.NO_BORDER);
        pc.setFixedHeight(18);
        testTable.addCell(pc);
        
        //--success percentage
        pc = new PdfPCell(new Phrase(String.valueOf(method.getSuccessPercentage()), sagoe10));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBorder(Rectangle.NO_BORDER);
        pc.setFixedHeight(18);
        testTable.addCell(pc);
        
        //--Time taken
        pc = new PdfPCell(new Phrase(calculateTimeTaken(res),sagoe10));
        pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        pc.setBorder(Rectangle.NO_BORDER);
        pc.setFixedHeight(18);
        testTable.addCell(pc);
        
        
        
        //Test objective
	     //sett header
	       pc = new PdfPCell(new Phrase("Test Objective",sagoe10Bold));
	       pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	       pc.setBackgroundColor(testTableclr);
	       pc.setBorder(Rectangle.TOP);
	       pc.setColspan(4);
	       testTable.addCell(pc);
	     //sett value
	       Object[] params = res.getParameters();
	       if(params.length >= 1){
	    	 @SuppressWarnings("unchecked")
			HashMap<String,String> xlData =  (HashMap<String, String>) params[0];
	         pc = new PdfPCell(new Phrase(xlData.get("OBJECTIVE"),sagoe10));
	       }
	       else{
	       	pc = new PdfPCell(new Phrase("Not Specified",sagoe10));
	       }
	       
	       pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	       pc.setColspan(4);
	       pc.setBorder(Rectangle.NO_BORDER);
	       pc.setFixedHeight(18);
	       testTable.addCell(pc);
        
        //--test description
	       //sett header
	       pc = new PdfPCell(new Phrase("Description ",sagoe10Bold));
	       pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	       pc.setBackgroundColor(testTableclr);
	       pc.setBorder(Rectangle.TOP);
	       pc.setColspan(4);
	       testTable.addCell(pc);
	       //sett value
	       pc = new PdfPCell(new Phrase(method.getDescription(), sagoe10));
	       pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	       pc.setColspan(4);
	       pc.setBorder(Rectangle.NO_BORDER);
	       pc.setFixedHeight(18);
	       testTable.addCell(pc);
	       
        pdf.add(testTable);
    }
    
    
    private  void createParameterTable(ITestResult res) throws DocumentException{
    	
    	PdfPTable paramTable = new PdfPTable(new float[]{.2f, .4f,.2f, .4f});
    	paramTable.setWidthPercentage(80);
    	paramTable.setSpacingBefore(5);
    	paramTable.setHorizontalAlignment(Element.ALIGN_CENTER);
    	
    	//Set table header....
    	PdfPCell pc = new PdfPCell(new Phrase("Data Used for Testing",sagoe14Bold));
        pc.setHorizontalAlignment(Element.ALIGN_CENTER);
        pc.setBackgroundColor(new BaseColor(125, 158, 192));
        pc.setBorder(Rectangle.BOTTOM);
        pc.setColspan(4);
        paramTable.addCell(pc);
        
        Object[] parameters = res.getParameters();
        boolean hasParameters = parameters != null && parameters.length > 0;
        
        if (hasParameters) {
        	int i=0;
        	int actualColCount=0;
        	//convert the first parameter to HashMap
        	@SuppressWarnings("unchecked")
			HashMap<String,String> xlData =  (HashMap<String, String>) parameters[0];
        	
        	for (Map.Entry<String, String> entry : xlData.entrySet()) {
        		
        	    String key = entry.getKey();
        	    String value = entry.getValue();
        	    
        	    
        	    
        	    //if not key is QUERY
        	    if( !(key.trim().toUpperCase().contains("QUERY")) && !(key.trim().equalsIgnoreCase("OBJECTIVE")) ){
	        	    pc 	= new PdfPCell(new Phrase(key, sagoe10));
	                pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	                pc.setBackgroundColor(testTableclr);
	                pc.setBorder(Rectangle.BOTTOM);
	                paramTable.addCell(pc);
	                
	        		pc = new PdfPCell(new Phrase(value, sagoe10));
	                pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	                pc.setBorder(Rectangle.BOTTOM);
	                paramTable.addCell(pc);
	                //increse the coount if not QUERY AND OBJ column
	                actualColCount++;
        	    }
        	}
        	
        	//This is to make table complete 
        	while(i < (actualColCount % 2)){
        		
        		//param key
        		pc 	= new PdfPCell(new Phrase("-" , sagoe10));
                pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                pc.setBackgroundColor(testTableclr);
                pc.setBorder(Rectangle.BOTTOM);
                paramTable.addCell(pc);
                
                //param value
                pc = new PdfPCell(new Phrase("-", sagoe10));
                pc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                pc.setBorder(Rectangle.BOTTOM);
                paramTable.addCell(pc);
                i++;
        	}
        	
        	
        	pdf.add(paramTable);
   	 	
        }
       
    }
    
    private int getPassedStepCount(ITestResult res) {
    	List<String> msgs = Reporter.getOutput(res);
    	int passtedSteps= 0;
    	
    	for (String line : msgs) {
    		String msgType = line.length() > 3 ? line.substring(line.length() - 3) : line;
    		if(msgType.equalsIgnoreCase("[P]")){
    			passtedSteps++;
    		}
    	}
    		
    	return passtedSteps;	
    }
    
    private int getFailedStepCount(ITestResult res) {
    	List<String> msgs = Reporter.getOutput(res);
    	int failedSteps= 0;
    	
    	for (String line : msgs) {
    		String msgType = line.length() > 3 ? line.substring(line.length() - 3) : line;
    		if(msgType.equalsIgnoreCase("[F]") || msgType.equalsIgnoreCase("[S]") ){
    			failedSteps++;
    		}
    	}
    		
    	return failedSteps;	
    	
    }
    
    private int getUncompletedCount(ITestResult res) {
    	List<String> msgs = Reporter.getOutput(res);
    	int ucSteps= 0;
    	
    	for (String line : msgs) {
    		String msgType = line.length() > 3 ? line.substring(line.length() - 3) : line;
    		if(msgType.equalsIgnoreCase("[U]")){
    			ucSteps++;
    		}
    	}
    		
    	return ucSteps;	
    }
    
    private int getWarningsCount(ITestResult res) {
    	List<String> msgs = Reporter.getOutput(res);
    	int warningSteps= 0;
    	
    	for (String line : msgs) {
    		String msgType = line.length() > 3 ? line.substring(line.length() - 3) : line;
    		if(msgType.equalsIgnoreCase("[W]")){
    			warningSteps++;
    		}
    	}
    		
    	return warningSteps;	
    }
    
    
private  void createMessageTable(ITestResult res) throws DocumentException, IOException{
    	
    	PdfPTable stepsTable = new PdfPTable(1);
    	Image passImg = null;
    	Image failImg = null;
    	Image warningImg = null;
    	Image infoImg = null;
    	Image attachImg = null;
    	Image ucImg = null;
  	  
    	int msgIndex = 1;
    	stepsTable.setWidthPercentage(72);
    	stepsTable.setSpacingBefore(5);
    	stepsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
    	
    	PdfPCell pc = new PdfPCell(new Phrase("Steps Performed", sagoe14Bold));
    	pc.setHorizontalAlignment(Element.ALIGN_CENTER);
        pc.setBackgroundColor(new BaseColor(125, 158, 192));
        pc.setBorder(Rectangle.BOTTOM);
        stepsTable.addCell(pc);
   	 	
        List<String> msgs = Reporter.getOutput(res);
        boolean hasReporterOutput = msgs.size() > 0;
        Throwable exception=res.getThrowable();
        boolean hasThrowable = exception!=null;
        
        //traverse thru all messages if messages available..
        if (hasReporterOutput||hasThrowable) {
        	//Initialize all images
        	passImg = Image.getInstance(settings.getProperty("IMAGES_PDF") + "pass.jpg");
        	passImg.scaleAbsolute(52f, 52f);
        	passImg.setAlignment(Image.BOTTOM);
        	
        	failImg = Image.getInstance("resources//images//pdf//" + "fail.jpg");
        	failImg.scaleAbsolute(52f, 52f);
        	failImg.setAlignment(Image.BOTTOM);
        	
        	warningImg = Image.getInstance("resources//images//pdf//" + "warning.jpg");
        	warningImg.scaleAbsolute(30f, 30f);
        	warningImg.setAlignment(Image.ALIGN_CENTER);
        	
        	infoImg = Image.getInstance("resources//images//pdf//" + "info.jpg");
        	infoImg.scaleAbsolute(30f, 30f);
        	infoImg.setAlignment(Image.ALIGN_CENTER);
        	
        	ucImg = Image.getInstance("resources//images//pdf//" + "uc.jpg");
        	ucImg.scaleAbsolute(30f, 30f);
        	ucImg.setAlignment(Image.ALIGN_CENTER);
        	
        	for (String line : msgs) {
        		
        		String msgType = line.length() > 3 ? line.substring(line.length() - 3) : line;
        		String message = line.length() > 3 ? line.substring(0, (line.length()-3)) : line;
        		Paragraph stepPara = new Paragraph();
        		pc = new PdfPCell();
        		pc = new PdfPCell(stepPara);
        		
        		if(msgType.equalsIgnoreCase("[P]")){
        			stepPara.add(new Chunk(passImg,-2,-2));
        			stepPara.add(new Chunk("Step " + String.valueOf(msgIndex) + ": " + message , sagoe10));
        			pc.setIndent(15);
        			pc.setFixedHeight(15);
        			msgIndex++;
        		}
        		else if(msgType.equalsIgnoreCase("[F]")){	
        			stepPara.add(new Chunk(failImg,-2,-2));
        			stepPara.add(new Chunk("Step " + String.valueOf(msgIndex) + ": " + message , sagoe10));
        			pc.setIndent(15);
        			pc.setFixedHeight(15);
        			msgIndex++;
        		}
        		else if(msgType.equalsIgnoreCase("[I]")){	
        			stepPara.add(new Chunk(infoImg,-2,-2));
        			
        			if(message.length()>72){
        				stepPara.add(new Chunk(message.substring(0, 72) + "...", sagoe10));
        			}
        			else{
        				stepPara.add(new Chunk(message, sagoe10));
        			}
        				
        			pc.setIndent(53);
        		}
        		else if(msgType.equalsIgnoreCase("[W]")){	
        			stepPara.add(new Chunk(warningImg,-2,-2));
        			stepPara.add(new Chunk(message, sagoe10));
        			pc.setIndent(53);
        			pc.setFixedHeight(15);
        		}
        		else if(msgType.equalsIgnoreCase("[J]")){	
        			attachImg = Image.getInstance("resources//images//screenshots//" + message.trim() + ".jpg");
        			attachImg.scalePercent(45f);
        			
        			stepPara.add(new Chunk(attachImg, 0 , -100));
        			pc.setIndent(55);
        			pc.setFixedHeight(110);
        		}
        		else if(msgType.equalsIgnoreCase("[U]")){	
        			
        			stepPara.add(new Chunk(ucImg,-2,-2));
        			stepPara.add(new Chunk(message, sagoe10));
        			pc.setIndent(15);
        			pc.setFixedHeight(15);
        			
        			
        		}
        		else if(msgType.equalsIgnoreCase("[S]")){	
        			stepPara.add(new Chunk(ucImg,-2,-2));
        			stepPara.add(new Chunk("Step " + String.valueOf(msgIndex) + ": " + message , sagoe10));
        			pc.setIndent(15);
        			pc.setFixedHeight(15);
        			msgIndex++;
        		}
        		else {	
        			stepPara.add(new Chunk("Other Step " + String.valueOf(msgIndex) + ": " , sagoe10));
        			stepPara.add(new Chunk(message, sagoe10));
        			msgIndex++;
        		}
        		
        	   
        	   pc.setHorizontalAlignment(Element.ALIGN_BOTTOM);
        	   pc.setBorder(Rectangle.NO_BORDER);
        	   stepsTable.addCell(pc);
                
            }
        	
        	pdf.add(stepsTable);
        }
       
    }


 	
    
    private  void addLableValue(String lable, String value) throws DocumentException {
    	
	   	Paragraph para = new Paragraph();
	   	Chunk ck = new Chunk(lable,sagoe10Bold);
	       para.add(ck);
	       ck = new Chunk(value,sagoe10);
	       para.add(ck);
	       pdf.add(para);
		
    	
    }
    
 
    private  void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
        }
      }
    
    
    // Inner class
    private class TestSorter implements Comparator<ITestResult> {

      public int compare(ITestResult o1, ITestResult o2) {
    	  	return (int) (o1.getStartMillis() - o2.getEndMillis());
      }
    }

 
}

