package md.ceiti.ms.hibernate.model.report;

import md.ceiti.ms.hibernate.model.dao.ToysDAO;
import md.ceiti.ms.hibernate.model.dao.impl.ToysDAOImpl;
import md.ceiti.ms.hibernate.model.entity.Toys;
import md.ceiti.ms.hibernate.util.HibernateUtil;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import javax.swing.*;
import java.io.InputStream;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ReportExporter {

    public static void generatePdfReport() {
        ToysDAO toysDAO = new ToysDAOImpl();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Toys>  toys = toysDAO.findAllAll();

            if (toys == null || toys.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nu există date disponibile pentru raportul PDF", "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String jrxmlFile = "/md/ceiti/ms/hibernate/reports/tttoy.jrxml";
            String jasperFile = "/md/ceiti/ms/hibernate/reports/tttoy.jasper";
            JasperReport jasperReport = null;

            try {
                InputStream jrxmlStream = ReportExporter.class.getResourceAsStream(jrxmlFile);
                if (jrxmlStream == null) {
                    throw new JRException("Nu s-a putut găsi fișierul JRXML: " + jrxmlFile);
                }
                jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            } catch (JRException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Eroare la compilarea JRXML: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(toys));

            String outputPdfFile = "ToyRaport.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPdfFile);

            JasperViewer.viewReport(jasperPrint, false);

            JOptionPane.showMessageDialog(null, "Raportul PDF a fost generat cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Eroare la generarea raportului PDF: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }
}