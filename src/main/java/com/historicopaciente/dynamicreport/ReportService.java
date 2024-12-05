package com.historicopaciente.dynamicreport;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import org.springframework.stereotype.Service;

import javax.management.timer.TimerMBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://github.com/teamjft/Dynamic-Jasper-Tricks/blob/master/src/main/java/com/vivek/DynamicJasperSample.java
@Service
public class ReportService {

    public byte[] generateReport() {
        try {

            JasperReport jasperReport;
            JasperDesign design = createDesign();

            List<Person> personList = new ArrayList<Person>();
            for (int i = 1; i <= 10; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("firstName", "Vivek " + i);
                map.put("lastName", "Yadav " + i);
                map.put("age", 20);
                Person person = new Person(map);
                personList.add(person);
            }

            Map<String, Object> params = new HashMap<>();
            params.put("VALOR_FILTRO", "equipamento");
            params.put("LOGO", "C:\\Users\\andre\\IdeaProjects\\dynamic-report\\src\\main\\resources\\static\\logo.png");

            jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(personList));
            return JasperExportManager.exportReportToPdf(jasperPrint);

//            FastReportBuilder reportBuilder = new FastReportBuilder();
//            Page page = Page.Page_A4_Landscape();
//            reportBuilder.setTitle("Table name")
//                    .setPageSizeAndOrientation(page)
//                    .setUseFullPageWidth(true)
//                    .setReportName("Report Name");
//
//            for (int column = 1; column <= 5; column++) {
//                reportBuilder.addColumn("Column " + column, "key" + column,
//                        String.class.getName(),
//                        30);
//            }
//
//            List rowsDataList = new ArrayList();
//
//            for (int row = 1; row <= 5; row++) {
//                HashMap<String, String> rowHashMap = new HashMap<>();
//                for (int column = 1; column <= 5; column++) {
//                    rowHashMap.put("key" + column, "Row" + row + " Column " + column);
//                }
//                rowsDataList.add(rowHashMap);
//            }
//
//            DynamicReport dynamicReport = reportBuilder.build();
//            JasperPrint finalReport = DynamicJasperHelper.generateJasperPrint(dynamicReport,
//                    new ClassicLayoutManager(),
//                    rowsDataList);
//
//            return JasperExportManager.exportReportToPdf(finalReport);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar o relatório", e);
        }
    }

    private JasperDesign createDesign() throws JRException {
        JasperDesign jasperDesign = new JasperDesign();
        /*Set basic design of page.*/
        jasperDesign.setName("sampleDynamicJasperDesign");
        jasperDesign.setPageWidth(595); // page width
        jasperDesign.setPageHeight(842); // page height
        jasperDesign.setColumnWidth(515);   // column width of page
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(40);
        jasperDesign.setRightMargin(40);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);

        JRDesignExpression expression = new JRDesignExpression();

        //Set style of page.
        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("Sans_Normal");
        normalStyle.setDefault(true);
        //normalStyle.setFontName("DejaVu Sans");
        normalStyle.setFontSize(Float.valueOf("12"));
        normalStyle.setPdfFontName("Helvetica");
        normalStyle.setPdfEncoding("Cp1252");
        normalStyle.setPdfEmbedded(false);
        jasperDesign.addStyle(normalStyle);


        /*
         * Generate field dynamically
         * */

        JRDesignField field = new JRDesignField();
        field.setName("firstName");
        field.setValueClass(String.class);
        jasperDesign.addField(field);

        field = new JRDesignField();
        field.setName("lastName");  // set name for field.
        field.setValueClass(String.class);  // set class for field. Its always depends upon data type which we want to get in this field.
        jasperDesign.addField(field);   // Added field in design.

        field = new JRDesignField();
        field.setName("age");
        field.setValueClass(Integer.class);
        jasperDesign.addField(field);

        JRDesignParameter parameter = new JRDesignParameter();
        parameter.setName("VALOR_FILTRO");
        parameter.setValueClass(String.class);
        jasperDesign.addParameter(parameter);

        JRDesignParameter parameterLogo = new JRDesignParameter();
        parameterLogo.setName("LOGO");
        parameterLogo.setValueClass(String.class);
        jasperDesign.addParameter(parameterLogo);

        JRDesignBand band;

        //Title Band
        band = new JRDesignBand();
        band.setHeight(30);

        JRDesignStaticText filtro1 = new JRDesignStaticText();
        filtro1.setText("FILTRO");
        filtro1.setX(0);
        filtro1.setY(0);
        filtro1.setHeight(20);
        filtro1.setWidth(515);
        filtro1.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        band.addElement(filtro1);
        jasperDesign.setTitle(band);

        JRDesignTextField valorFiltro1 = new JRDesignTextField();
        valorFiltro1.setX(0);  // x position of text field.
        valorFiltro1.setY(6);  // y position of text field.
        valorFiltro1.setWidth(160);    // set width of text field.
        valorFiltro1.setHeight(20);    // set height of text field.

        JRDesignExpression jrExpression1 = new JRDesignExpression(); // new instanse of expression. We need create new instance always when need to set expression.
        jrExpression1.setText("$P{VALOR_FILTRO}"); //  Added String before field in expression.
        valorFiltro1.setExpression(jrExpression1);  // set expression value in textfield.
        band.addElement(valorFiltro1); // Added element in textfield.

        //Criar o componente de imagem
        JRDesignImage image = new JRDesignImage(null);
        image.setX(0); // Posição horizontal
        image.setY(0); // Posição vertical
        image.setWidth(50); // Largura da imagem
        image.setHeight(50); // Altura da imagem
        image.setScaleImage(ScaleImageEnum.RETAIN_SHAPE); // Escala para manter proporção
        image.setHorizontalImageAlign(HorizontalImageAlignEnum.CENTER);

        JRDesignExpression expressionImg = new JRDesignExpression();
        expressionImg.setText("$P{LOGO}"); // Parâmetro que conterá o caminho ou os dados binários da imagem
        image.setExpression(expressionImg);

        band.setHeight(50);
        band.addElement(image);

        JRDesignStaticText staticText = new JRDesignStaticText();
        staticText.setText("Person's Specification");
        staticText.setX(0);
        staticText.setY(0);
        staticText.setHeight(20);
        staticText.setWidth(515);
        staticText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        band.addElement(staticText);
        jasperDesign.setTitle(band);


//        Detail Band
        band = new JRDesignBand(); // New band
        band.setHeight(20); // Set band height

        /*Create text field dynamically*/
        JRDesignTextField textField = new JRDesignTextField();
        textField.setX(0);  // x position of text field.
        textField.setY(0);  // y position of text field.
        textField.setWidth(160);    // set width of text field.
        textField.setHeight(20);    // set height of text field.
        JRDesignExpression jrExpression = new JRDesignExpression(); // new instanse of expression. We need create new instance always when need to set expression.
        jrExpression.setText("\"" + "First Name: " + "\"" + "+" + "$F{firstName}"); //  Added String before field in expression.
        textField.setExpression(jrExpression);  // set expression value in textfield.
        band.addElement(textField); // Added element in textfield.

        textField = new JRDesignTextField();
        textField.setX(160);
        textField.setY(0);
        textField.setWidth(160);
        textField.setHeight(20);
        jrExpression = new JRDesignExpression();
        jrExpression.setText("$F{lastName}" + "+" + "\"" + " :Last Name" + "\""); // Added string after field value
        textField.setExpression(jrExpression);
        band.addElement(textField);

        textField = new JRDesignTextField();
        textField.setX(320);
        textField.setY(0);
        textField.setWidth(160);
        textField.setHeight(20);
        jrExpression = new JRDesignExpression();
        String age = "\"" + "<html><font color=" + "\\" + "\"" + "#66FF33" + "\\" + "\"" + ">" + "\"" + "+" + "\"" + "Age is: " + "\"" + "+" + "\"" + "</font><font color=" + "\\" + "\"" + "#6600FF" + "\\" + "\"" + ">" + "\"" + "+" + "$F{age}" + "+" + "\"" + "</font></html>" + "\"";  // added html in text field with different color.
        jrExpression.setText(age);
        textField.setExpression(jrExpression);
        textField.setMarkup("html"); // By Default markup is none, We need to set it as html if we set expression as html.
        band.addElement(textField);
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);


        return jasperDesign;
    }
}
