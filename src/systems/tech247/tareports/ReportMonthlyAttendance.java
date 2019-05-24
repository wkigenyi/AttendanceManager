/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.tareports;

import net.sf.dynamicreports.adhoc.AdhocManager;
import net.sf.dynamicreports.adhoc.configuration.AdhocConfiguration;
import net.sf.dynamicreports.adhoc.configuration.AdhocReport;
import net.sf.dynamicreports.adhoc.report.DefaultAdhocReportCustomizer;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.ReportBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import systems.tech247.dbaccess.DataAccess;

/**
 *
 * @author Wilfred
 */
public class ReportMonthlyAttendance {
    
    String from;
    String to;
    
    ColumnBuilder[] columns;
    String[] columnNames;
    DRDataSource data; 
    JasperReportBuilder reportBuilder;

    public ReportMonthlyAttendance(ColumnBuilder[] columns,String[] columnNames,DRDataSource data,String from,String to) {
        this.columnNames = columnNames;
        this.columns = columns;
        this.data = data;
        this.from = from;
        this.to = to;
        build();
    }
    
    private void build(){
        AdhocConfiguration configuration = new AdhocConfiguration();
                          
	//Start;
	AdhocReport report = new AdhocReport();
	configuration.setReport(report);
            try{
                reportBuilder = AdhocManager.createReport(configuration.getReport(),new ReportCustomizer());
                reportBuilder.setTemplate(ReportTemplate.reportTemplate)
                    .columns(columns)
                    .setDataSource(data);
                    
                
                    reportBuilder.groupBy((TextColumnBuilder)columns[4]);
                
                
            
            
                
                
                
                if(columns.length >= 13){
                    reportBuilder.setPageFormat(PageType.A3,PageOrientation.LANDSCAPE);
                }
            
            }catch(DRException ex){
                
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
                        try{
                            report.title(ReportTemplate.createTitleComponent(DataAccess.getDefaultCompany(),"Monthly Attendance Summary",from,to));
                        }catch(Exception ex){
                        }
			report.setTemplate(ReportTemplate.reportTemplate);
                        report.pageFooter(ReportTemplate.footerComponent);
                            
		}

		

		

	}
            
        public JasperReportBuilder getReport(){
            return reportBuilder;
        }
    
}
