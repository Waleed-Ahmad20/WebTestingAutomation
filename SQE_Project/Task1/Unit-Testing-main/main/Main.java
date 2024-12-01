package main;
import BL.BO;
import DAL.DAOFacade;
import DAL.DAOFactory;
import PL.PO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String factoryClassName = properties.getProperty("dao.factory.class");
        DAOFactory factory;
        try {
            factory = (DAOFactory) Class.forName(factoryClassName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        DAOFacade DAOFacade = factory.createDAO();
        BO BO = new BO(DAOFacade);
        new PO(BO);
    }
}