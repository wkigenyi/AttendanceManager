/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.tareports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.dynamicreports.adhoc.AdhocManager;
import net.sf.dynamicreports.adhoc.configuration.AdhocConfiguration;
import net.sf.dynamicreports.adhoc.configuration.AdhocReport;
import net.sf.dynamicreports.adhoc.report.DefaultAdhocReportCustomizer;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.ReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.openide.awt.StatusDisplayer;
import systems.tech247.dbaccess.DataAccess;
import systems.tech247.hr.OrganizationUnits;
import systems.tech247.reports.ReportTemplate;

/**
 *
 * @author Admin
 */
public class ReportAttendanceSummary {
    
    
    
    JasperReportBuilder reportBuilder;
    JRDataSource attData;
    Date from;
    Date to;
    
    
    String title;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    public ReportAttendanceSummary(JRDataSource data,Date from, Date to){
        
        this.attData = data;
        this.from = from;
        this.to = to;
         
        title = "TRX Template";
        
        build();
    }
    
    
    private void build() {
                
                AdhocConfiguration configuration = new AdhocConfiguration();
                          
		//Start;
		AdhocReport report = new AdhocReport();
		configuration.setReport(report);
                
		
                
                
                try{
                    reportBuilder = AdhocManager.createReport(configuration.getReport(),new ReportCustomizer());
                    reportBuilder.setTemplate(ReportTemplate.reportTemplate)
                            
                            .setPageFormat(PageType.A4,PageOrientation.LANDSCAPE)
//                            .addDetail(DynamicReports.cmp.verticalList(
//                                    DynamicReports.cmp.text("Name : "+emp.getSurName()+" "+emp.getOtherNames()),
//                                    DynamicReports.cmp.text("PR No: "+emp.getEmpCode()),
//                                    DynamicReports.cmp.text("Dept : "+emp.getOrganizationUnitID().getOrganizationUnitName())
//                            ))
                            
           
                            .columns(
                                    DynamicReports.col.reportRowNumberColumn("#").setFixedColumns(2),
                                    DynamicReports.col.column("PR no.","empcode",DynamicReports.type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setFixedWidth(40),
                                    DynamicReports.col.column("NAME","empname",DynamicReports.type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(120),
                                    DynamicReports.col.column("DEPT","dept",DynamicReports.type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(130),
                                    DynamicReports.col.column("DESIG","job",DynamicReports.type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setFixedWidth(130),
                                    DynamicReports.col.column("JOINED","join",DynamicReports.type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
                                    DynamicReports.col.column("DAYS","days",DynamicReports.type.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setFixedWidth(40),
                                    DynamicReports.col.column("ABS","absent",DynamicReports.type.integerType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setFixedWidth(40),
                                    DynamicReports.col.column("CATEGORY","cat",DynamicReports.type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setFixedWidth(128),
                                    DynamicReports.col.column("BAS PAY","basic",DynamicReports.type.doubleType()).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT),
                                    DynamicReports.col.column("AMOUNT","amount",DynamicReports.type.doubleType()).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
                                    
                            )
                            
                            .setReportName("Template")
                                    
                                    
                            .setDataSource(attData);
                            
                            //.toPdf(new FileOutputStream("C:/Payslips/PaySlip.pdf"));
                            
                            
                            
                            
                    
                }catch(DRException ex){
                    StatusDisplayer.getDefault().setStatusText(ex.getMessage());
                }
	}
        
        
        
        
        
        private class ReportCustomizer extends DefaultAdhocReportCustomizer {

		/**
		 * If you want to add some fixed content to a report that is not needed to store in the xml file.
		 * For example you can add default page header, footer, default fonts,...
		 */
            
   
            
		@Override
		public void customize(ReportBuilder<?> report, AdhocReport adhocReport) throws DRException {
			super.customize(report, adhocReport);
			// default report values
			report.setTemplate(AttReportSummaryTemplate.reportTemplate);
                        try{
                            report.title(
                                    AttReportSummaryTemplate.createTitleComponent(DataAccess.getDefaultCompany(),sdf.format(from),sdf.format(to),title)
                                  
                            );
                        }catch(IOException ex){
                            
                        }
			// a fixed page footer that user cannot change, this customization is not stored in the xml file
			//report.pageFooter(ReportTemplate.footerComponent);
		}

		

		

	}
        
        public JasperReportBuilder getReport(){
            return reportBuilder;
        }
}
