import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;


public class OpenPorfolio extends AbstractTableModel {

    private final List<Stocks> stocksList;

    private final String[] columnNames = new String[]{
        "Weight", "Stock"
    };

    private final Class[] columnClass = new Class[]{
        Integer.class, String.class
    };

    public OpenPorfolio(List<Stocks> stocksList){
        this.stocksList = stocksList;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public int getRowCount() {
        return stocksList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Stocks row = stocksList.get(rowIndex);
        if(columnIndex==0){
            return row.getWeight();
        }
        else if(columnIndex==1){
            return row.getName();
        }
        return null;
    }

    public Class getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Stocks row = stocksList.get(rowIndex);
        if(columnIndex==0){
            row.addWeight((Integer) aValue);
        }
    }
}
