module md.ceiti.ms.hibernate {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;
    requires java.naming;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires jasperreports;
    // requires net.sf.jasperreports.core;

    opens md.ceiti.ms.hibernate.main to javafx.fxml;
    opens md.ceiti.ms.hibernate.controller to javafx.fxml;
    opens md.ceiti.ms.hibernate.view to javafx.fxml;
    opens md.ceiti.ms.hibernate.model.entity to org.hibernate.orm.core , javafx.base;

    exports md.ceiti.ms.hibernate.main;
    exports md.ceiti.ms.hibernate.model.entity;
    exports md.ceiti.ms.hibernate.controller;
}