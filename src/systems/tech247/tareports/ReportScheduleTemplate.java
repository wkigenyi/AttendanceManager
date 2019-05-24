/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.tareports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import net.sf.dynamicreports.adhoc.AdhocManager;
import net.sf.dynamicreports.adhoc.configuration.AdhocConfiguration;
import net.sf.dynamicreports.adhoc.configuration.AdhocReport;
import net.sf.dynamicreports.adhoc.report.DefaultAdhocReportCustomizer;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.ReportBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
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
public class ReportScheduleTemplate {
    
    
    
    JasperReportBuilder reportBuilder;
    JRDataSource attData;
    OrganizationUnits unit;
    ColumnBuilder[] columns;
    
    
    String title;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    public ReportScheduleTemplate(JRDataSource data,OrganizationUnits unit,ColumnBuilder[] columns){
        
        this.attData = data;
        this.unit = unit; 
        this.columns = columns;
        title = "Schedule Template";
        
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
                            
           
                            .columns(columns                              
                                    
                                    
                                    
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
			report.setTemplate(AttScheduleTemplate.reportTemplate);
                        try{
                            report.title(
                                    AttScheduleTemplate.createTitleComponent(DataAccess.getDefaultCompany(),unit.getOrganizationUnitName(),title)
                                  
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
