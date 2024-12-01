package DAL;

public class MySQLDAOFactory implements DAOFactory {
    @Override
    public DAOFacade createDAO() {
        return new DAO();
    }
}