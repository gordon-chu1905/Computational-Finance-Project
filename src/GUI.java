
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

public class GUI {

    //Swing variables
    private JTextField fileTextField;
    private JButton browseButton;
    private JButton pickStockButton;
    private JButton openButton;
    private JCheckBox standardCheckBox;
    private JCheckBox blumeCheckBox;
    private JButton estimateButton;
    private JTextField fixedReturnTextField;
    private JTextField riskFreeRateTextField;
    private JButton optimizeButton;
    private JLabel estimationMethodLabel;
    private JLabel optimizationLabel;
    private JLabel fixedReturnLabel;
    private JLabel riskFreeRateLabel;
    private JLabel fileDirectoryLabel;
    private JPanel panel;
    private JTextField startDateTextField;
    private JTextField endDateTextField;
    private JLabel startDateLabel;
    private JLabel endDateLabel;

    //Data variables
    private ArrayList<File> stocks = new ArrayList<File>();
    private File[] stockSelected;
    private List<Stocks> stocksData;

    private double portfolioExpectedReturn;
    private double portfolioRisk;
    private double systematicRisk;
    private double portfolioVariance;

    private int stockCount = 0;

    //functions

    public GUI(){

        //When "Browse" button clicked
        browseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    stocks.add(openFile());
                    System.out.println("Stock file selected");

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        //When "Pick Stock" Button clicked
        pickStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //pick Stock Interface
                    JFrame pickStockFrame = new JFrame("Select Stock Dialog");
                    JPanel pickStockPanel = new JPanel();
                    pickStockPanel.setSize(100,100);
                    String[] nameList = new String[stocks.size()];
                    for(int i=0; i<stocks.size(); i++) {
                        nameList[i] = stocks.get(i).getName();
                    }
                    JList stocksList = new JList(nameList);
                    stocksList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    pickStockPanel.add(stocksList);
                    JButton submitPickedStocks = new JButton("Submit");
                    pickStockPanel.setLayout(new FlowLayout());
                    pickStockPanel.add(submitPickedStocks);
                    pickStockFrame.setContentPane(pickStockPanel);
                    pickStockFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    pickStockFrame.pack();
                    pickStockFrame.setVisible(true);

                    submitPickedStocks.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            int[] select = stocksList.getSelectedIndices();
                            stockSelected = new File[select.length];
                            for(int i=0;i<select.length;i++){
                                stockSelected[i] = stocks.get(select[i]);
                            }
                            for(int i=0; i < stockSelected.length; i++){
                                System.out.println(stockSelected[i].getName());
                            }

                            pickStockFrame.setVisible(false);
                            pickStockFrame.dispose();
                        }
                    });


                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });


        //When "Open" Button clicked
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stocksData = stocksDataMaking(stockSelected);
                    JTextField[] inputTextField = new JTextField[stockSelected.length];
                    JLabel[] inputLabel = new JLabel[stockSelected.length];
                    JPanel openStackPanel = new JPanel(new GridLayout(0, 2));

                    for (int i = 0; i < stockSelected.length; i++) {
                        inputLabel[i] = new JLabel(stockSelected[i].getName().replace(".txt", ""));
                        inputTextField[i] = new JTextField(10);
                    }

                    JFrame openJFrame = new JFrame("Manage Portfolio Dialog");
                    JPanel openOuterJPanel = new JPanel(new BorderLayout());
                    JPanel openJPanelBottom = new JPanel(new BorderLayout());
                    openJPanelBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    JButton openSubmitButton = new JButton();
                    openSubmitButton.setText("Submit");
                    openOuterJPanel.add(openJPanelBottom, BorderLayout.SOUTH);

                    for (int i = 0; i < stockSelected.length; i++) {
                        openStackPanel.add(inputLabel[i]);
                        openStackPanel.add(inputTextField[i]);
                    }

                    openJPanelBottom.add(openSubmitButton);
                    openOuterJPanel.add(openStackPanel, BorderLayout.NORTH);
                    openOuterJPanel.add(openSubmitButton, BorderLayout.SOUTH);
                    openJFrame.setContentPane(openOuterJPanel);
                    openJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    openJFrame.pack();
                    openJFrame.setVisible(true);

                    //When "Submit" Button clicked
                    openSubmitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                double weightCount = 0;
                                for (int i = 0; i < stockSelected.length; i++) {
                                    weightCount += Double.parseDouble(inputTextField[i].getText());
                                }

                                if (weightCount == 1) {

                                    for (int i = 0; i < stockSelected.length; i++) {
                                        stocksData.get(i).addWeight(Double.parseDouble(inputTextField[i].getText()));
                                    }


                                } else{

                                }

                                openJFrame.setVisible(false);
                                openJFrame.dispose();

                            }catch(Exception exception){
                                exception.printStackTrace();
                            }


                        }
                    });

                }catch (Exception exception){
                    exception.printStackTrace();
                }

            }
        });

        //When "Estimate" Button Clicked
        estimateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{

                    int startDate, endDate;
                    startDate = Integer.parseInt(startDateTextField.getText());
                    endDate = Integer.parseInt(endDateTextField.getText());

                    //stocks data are in File[] format, not yet extracted to calculate highest, lowest, etc.
                    //only entered data in stocksData is their weight and name respectively (assume index of both array are same)
                    //(use index to match data), Stocks Type in Stocks.java

                    if(standardCheckBox.isSelected()){

                        extractFileData(stockSelected, startDate, endDate);

                        JFrame classicJFrame = new JFrame();
                        JPanel classicJPanel = new JPanel();
                        JButton close = new JButton("Close");

                        String[][] data = new String[stocksData.size()+2][8];

                        data[0][0] = "Weight";
                        data[0][1] = "Name";
                        data[0][2] = "StartDate";
                        data[0][3] = "EndDate";
                        data[0][4] = "Highest";
                        data[0][5] = "Lowest";
                        data[0][6] = "Return";
                        data[0][7] = "Risk";

                        for(int i=0; i<stocksData.size(); i++) {
                            data[i+1][0] = stocksData.get(i).getWeight() + "";
                            data[i+1][1] = stocksData.get(i).getName();
                            data[i+1][2] = stocksData.get(i).getStartDate() + "";
                            data[i+1][3] = stocksData.get(i).getEndDate() + "";
                            data[i+1][4] = stocksData.get(i).getHighest() + "";
                            data[i+1][5] = stocksData.get(i).getLowest() + "";
                            data[i+1][6] = stocksData.get(i).getReturnRate() + "";
                            data[i+1][7] = stocksData.get(i).getRiskRate() + "";
                        }

                        data[stocksData.size()+1][6] = portfolioExpectedReturn+"";
                        data[stocksData.size()+1][7] = portfolioRisk+"";
                        systematicRisk = 0;

                        String[] column = {"Weight", "Name", "StartDate", "EndDate", "Highest", "Lowest", "Return", "Risk"};
                        double temp = 0;
                        for(int i=0; i<stocksData.size(); i++){
                            for(int j=0; j<stocksData.size(); j++){
                                if(i != j){
                                    for(int n=0; n<stocksData.get(i).getRiskTime().size(); n++){
                                        systematicRisk = systematicRisk + (stocksData.get(i).getRiskTime().get(n) -
                                                stocksData.get(i).getReturnRate()) *
                                                (stocksData.get(j).getRiskTime().get(n)
                                                        - stocksData.get(j).getReturnRate());
                                    }
                                }
                            }
                        }

                        System.out.println("Test: "+systematicRisk);
                        systematicRisk = systematicRisk / (stocksData.size() - 1.00);
                        systematicRisk = systematicRisk * ((stocksData.size()-1.00) / stocksData.size());

                        data[stocksData.size()+1][0] = "SysRisk:";
                        data[stocksData.size()+1][1] = systematicRisk+"";
                        System.out.println(systematicRisk);

                        classicJPanel.setLayout(new FlowLayout(1));

                        JTable classicTable = new JTable(data, column);
                        classicJPanel.add(classicTable);
                        classicJPanel.add(close);
                        classicJFrame.setContentPane(classicJPanel);
                        classicJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        classicJFrame.pack();
                        classicJFrame.setVisible(true);

                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                    classicJFrame.setVisible(false);
                                    classicJFrame.dispose();
                            }
                        });




                    }else if(blumeCheckBox.isSelected()){

                        extractFileData(stockSelected, startDate, endDate);

                        systematicRisk = 0;

                        //Covariance mean of the portfolio (which is same as systematic risk)
                        for(int i=0; i<stocksData.size(); i++){
                            for(int j=0; j<stocksData.size(); j++){
                                if(i != j){
                                    for(int n=0; n<stocksData.get(i).getRiskTime().size(); n++){
                                        systematicRisk = systematicRisk + (stocksData.get(i).getRiskTime().get(n) -
                                                stocksData.get(i).getReturnRate()) *
                                                (stocksData.get(j).getRiskTime().get(n)
                                                        - stocksData.get(j).getReturnRate());
                                        System.out.println(systematicRisk);
                                    }
                                }
                            }
                        }


                        systematicRisk = systematicRisk / (stocksData.size() - 1.00);

                        System.out.println("Test: "+systematicRisk);
                        System.out.println("pv: "+portfolioVariance);

                        double rawBeta = (double) (systematicRisk)
                                / (portfolioVariance);
                        System.out.println("rb: "+rawBeta);
                        double adjustedBeta = (2.00/3.00) * rawBeta + 1.00/3.00;

                        JFrame blumeFrame = new JFrame();
                        JPanel blumePanel = new JPanel();
                        blumeFrame.setLayout(new FlowLayout(1));
                        blumePanel.add(new JLabel("Beta:"));
                        blumePanel.add(new JTextField(rawBeta+""));
                        blumePanel.add(new JLabel("Adjusted Beta:"));
                        blumePanel.add(new JTextField(adjustedBeta+""));
                        JButton close = new JButton("Close");
                        blumePanel.add(close);
                        blumeFrame.setContentPane(blumePanel);
                        blumeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        blumeFrame.pack();
                        blumeFrame.setVisible(true);

                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                blumeFrame.setVisible(false);
                                blumeFrame.dispose();
                            }
                        });


                    }

                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });

        //When "Optimize" Button clicked
        optimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double fixedReturn = Double.parseDouble(fixedReturnTextField.getText());
                double riskFreeRate = Double.parseDouble(riskFreeRateTextField.getText());
                double optimizedRisk = 0;
                double covariance = 0;
                double sharpeRatio = 0;

                for(int i=0; i<stocksData.size(); i++){
                    //Optimized Covariance
                    covariance = covariance + (stocksData.get(i).getRiskRate() - fixedReturn) *
                            (stocksData.get(i).getRiskRate() - portfolioExpectedReturn);


                    //Optimized Risk
                    optimizedRisk = optimizedRisk + Math.pow(stocksData.get(i).getRiskRate() - fixedReturn, 2);
                }

                optimizedRisk = optimizedRisk / (stocksData.size() - 1);
                optimizedRisk = Math.sqrt(optimizedRisk / (stocksData.size() - 1));
                covariance = covariance / (stocksData.size() - 1);

                sharpeRatio = (fixedReturn - riskFreeRate) / optimizedRisk;



                JFrame optimizedJFrame = new JFrame();
                JPanel optOuterPanel = new JPanel();
                JPanel optNorthPanel = new JPanel();

                JButton close = new JButton("Finish");



                Border border = BorderFactory.createTitledBorder("\"Optimization Results\"");
                optNorthPanel.setBorder(border);
                optNorthPanel.setLayout(new FlowLayout(2));
                optNorthPanel.add(new JLabel("Risk: "));
                optNorthPanel.add(new JTextField(optimizedRisk+""));
                optNorthPanel.add(new JLabel("Sharpe Ratio: "));
                optNorthPanel.add(new JTextField(sharpeRatio+""));
                optNorthPanel.add(new JLabel("with input Expected Return: "+fixedReturn+", Risk-free rate: "+riskFreeRate));
                optOuterPanel.add(optNorthPanel);
                optOuterPanel.setLayout(new FlowLayout(1));
                optOuterPanel.add(close);

                optimizedJFrame.setContentPane(optOuterPanel);
                optimizedJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                optimizedJFrame.pack();
                optimizedJFrame.setVisible(true);

                close.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        optimizedJFrame.setVisible(false);
                        optimizedJFrame.dispose();
                    }
                });


            }
        });

    }

    public File openFile(){
        final JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;

        } else {
            System.out.println("no file selected");
            return null;
        }
    }

    private List<Stocks> stocksDataMaking(File[] stockSelected){
        int stocksLength = stockSelected.length;
        List<Stocks> dataReturn = new ArrayList<Stocks>();
        Stocks temp;

        for(int i=0; i<stocksLength; i++){
            temp = new Stocks(stockSelected[i].getName().replace(".txt",""));
            dataReturn.add(temp);
        }

        return dataReturn;
    }

    //extract data from File[] to Stock[]
    private void extractFileData(File[] stockInput, int startDate, int EndDate) throws FileNotFoundException {
        portfolioVariance = 0;
        portfolioRisk = 0;
        portfolioExpectedReturn = 0;

        int[] dataList = new int[stockInput.length];
        String[] dataRow;
        double highest, lowest, sum, count, sumtemp;
        double expectedReturn;
        double risk;
        List<Double> riskTime = new ArrayList<Double>();

        for (int i = 0; i < stockInput.length ; i++) {

            sum = -1;
            count = 0;
            highest = 0;
            lowest = -1;
            risk = 0;

            Scanner scanner = new Scanner(stockInput[i]);
            if(scanner.hasNext()){
                scanner.nextLine();
            }

            while (scanner.hasNext()){
                dataRow = scanner.nextLine().split(",");

                if(EndDate > Integer.parseInt(dataRow[1]) && Integer.parseInt(dataRow[1]) > startDate) {

                    riskTime.add(Math.log(Double.parseDouble(dataRow[6]))-Math.log(Double.parseDouble(dataRow[3])));
                    sum = sum + (Math.log(Double.parseDouble(dataRow[6]))-Math.log(Double.parseDouble(dataRow[3])));
                    count++;



                    if (Double.parseDouble(dataRow[4]) > highest) {
                        highest = Double.parseDouble(dataRow[4]);
                    } else {
                    }

                    if (lowest == -1 || lowest > Double.parseDouble(dataRow[5])) {
                        lowest = Double.parseDouble(dataRow[5]);
                    } else {
                    }
                }

            }

            expectedReturn = sum / count;


            for(int j=0; j<riskTime.size(); j++){
                risk = risk + Math.pow(riskTime.get(j) - expectedReturn, 2);
            }

            risk = risk / (count - 1);
            risk = Math.sqrt(risk);

            stocksData.get(i).addData(startDate, EndDate, highest, lowest, expectedReturn, risk, riskTime);

            portfolioExpectedReturn = portfolioExpectedReturn + stocksData.get(i).getWeight() * expectedReturn;
        }

        for(int i=0; i<stockInput.length; i++){
            portfolioRisk = portfolioRisk + Math.pow(stocksData.get(i).getReturnRate() - portfolioExpectedReturn, 2);
        }
        portfolioVariance = portfolioRisk;
        portfolioRisk = Math.sqrt(portfolioRisk/(stockInput.length - 1));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    //main for execute
    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}

