
package raven.application.form.other;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import raven.cell.TableActionCellRender;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import raven.application.Application;
import raven.cell.TableActionCellEditor;
import raven.cell.TableActionEvent;
import raven.application.form.other.Invoice_Details;


public class Invoice_List extends javax.swing.JPanel {

    public Invoice_List() {
        initComponents();
          TableActionEvent event = new TableActionEvent() {
          
@Override
public void onEdit(int row) {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    
    Font labelFont = new Font("Arial", Font.BOLD, 16);

    // Row 1: Client and Reference Fields
    gbc.gridx = 0; gbc.gridy = 0;
    JLabel clientLabel = new JLabel("Client:");
    clientLabel.setFont(labelFont);
    panel.add(clientLabel, gbc);

    gbc.gridx = 1;
    JTextField clientField = new JTextField("Troy Walker", 15); 
    panel.add(clientField, gbc);

    gbc.gridx = 2;
    JLabel referenceLabel = new JLabel("Reference:");
    referenceLabel.setFont(labelFont);
    panel.add(referenceLabel, gbc);

    gbc.gridx = 3;
    JTextField referenceField = new JTextField("Reference-001", 15); 
    panel.add(referenceField, gbc);

    // Row 2: Select Products (with combo box extending all the way to the right)
    gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 6;
    JLabel selectProductsLabel = new JLabel("Select Products:");
    selectProductsLabel.setFont(labelFont); 
    panel.add(selectProductsLabel, gbc);

    gbc.gridy = 2;
    JComboBox<String> productCombo = new JComboBox<>(new String[]{
        "Plywood 12mm", "PVC Pipes 3 inch", "Acrylic Paint 5L", "Cement 50kg Bag", "Paint Roller Set"
    });
    gbc.gridwidth = 6; // Extend the combo box all the way to the right
    panel.add(productCombo, gbc);


    // Row 4: Product Table with hardware-related items
    gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 6;
    String[] columnNames = {"#", "Code", "Product Name", "Invoice Qty", "Price", "Unit Price", "Tax", "Subtotal", "Action"};
    Object[][] data = {
        {1, "AP-000001", "Plywood 12mm", 10, 300, "$3000", 150, "$3150", "Remove"},
        {2, "AP-000002", "PVC Pipes 3 inch", 20, 50, "$1000", 50, "$1050", "Remove"},
        {3, "AP-000003", "Acrylic Paint 5L", 5, 200, "$1000", 50, "$1050", "Remove"}
    };

    DefaultTableModel model = new DefaultTableModel(data, columnNames);
    JTable table = new JTable(model) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 3 || column == 8;  // Make only quantity and action columns editable
        }
    };

    // Custom quantity editor with plus/minus buttons
    TableColumn quantityColumn = table.getColumnModel().getColumn(3);
    quantityColumn.setCellEditor(new QuantityEditor(table, model));

    // Custom Remove button in the action column
    TableColumn actionColumn = table.getColumnModel().getColumn(8);
    actionColumn.setCellRenderer(new ButtonRenderer());
    actionColumn.setCellEditor(new ButtonEditor(new JCheckBox(), table, model));

    JScrollPane tableScrollPane = new JScrollPane(table);
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    panel.add(tableScrollPane, gbc);

    // Reset grid constraints for following components
    gbc.gridwidth = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;

    // Row 5: Discount Type, Discount, and Transport Cost Fields
    gbc.gridy = 5; gbc.gridx = 0;
    JLabel discountTypeLabel = new JLabel("Discount Type:");
    discountTypeLabel.setFont(labelFont);
    panel.add(discountTypeLabel, gbc);

    gbc.gridx = 1;
    JComboBox<String> discountTypeCombo = new JComboBox<>(new String[]{"Fixed", "Percentage"});
    panel.add(discountTypeCombo, gbc);

    gbc.gridx = 2;
    JLabel discountLabel = new JLabel("Discount:");
    discountLabel.setFont(labelFont);
    panel.add(discountLabel, gbc);

    gbc.gridx = 3;
    JTextField discountField = new JTextField(10);
    panel.add(discountField, gbc);

    gbc.gridx = 4;
    JLabel transportCostLabel = new JLabel("Transport Cost:");
    transportCostLabel.setFont(labelFont);
    panel.add(transportCostLabel, gbc);

    gbc.gridx = 5;
    JTextField transportCostField = new JTextField(10);
    panel.add(transportCostField, gbc);

    // Row 6: Invoice Tax, Total Tax, and Net Total Fields
    gbc.gridy = 6; gbc.gridx = 0;
    JLabel invoiceTaxLabel = new JLabel("Invoice Tax:");
    invoiceTaxLabel.setFont(labelFont);
    panel.add(invoiceTaxLabel, gbc);

    gbc.gridx = 1;
    JTextField invoiceTaxField = new JTextField("VAT@10", 10);
    panel.add(invoiceTaxField, gbc);

    gbc.gridx = 2;
    JLabel totalTaxLabel = new JLabel("Total Tax:");
    totalTaxLabel.setFont(labelFont);
    panel.add(totalTaxLabel, gbc);

    gbc.gridx = 3;
    JTextField totalTaxField = new JTextField("1872.50", 10);
    panel.add(totalTaxField, gbc);

    gbc.gridx = 4;
    JLabel netTotalLabel = new JLabel("Net Total:");
    netTotalLabel.setFont(labelFont);
    panel.add(netTotalLabel, gbc);

    gbc.gridx = 5;
    JTextField netTotalField = new JTextField("20597.50", 10); 
    netTotalField.setEditable(false);  // Calculated automatically
    panel.add(netTotalField, gbc);

    // Row 7: PO Reference, Payment Terms
    gbc.gridy = 7; gbc.gridx = 0;
    JLabel poReferenceLabel2 = new JLabel("PO Reference:");
    poReferenceLabel2.setFont(labelFont);
    panel.add(poReferenceLabel2, gbc);

    gbc.gridx = 1;
    JTextField poReferenceField2 = new JTextField("PO Ref-001", 15);
    panel.add(poReferenceField2, gbc);

    gbc.gridx = 2;
    JLabel paymentTermsLabel = new JLabel("Payment Terms:");
    paymentTermsLabel.setFont(labelFont);
    panel.add(paymentTermsLabel, gbc);

    gbc.gridx = 3;
    JTextField paymentTermsField = new JTextField("30 days", 10);
    panel.add(paymentTermsField, gbc);

    // Row 8: Delivery Place, Date, Status (just before the Note section)
    gbc.gridy = 8; gbc.gridx = 0;
    JLabel deliveryPlaceLabel = new JLabel("Delivery Place:");
    deliveryPlaceLabel.setFont(labelFont);
    panel.add(deliveryPlaceLabel, gbc);

    gbc.gridx = 1;
    JTextField deliveryPlaceField = new JTextField("Isulan, Sultan Kudarat", 10); 
    panel.add(deliveryPlaceField, gbc);

    gbc.gridx = 2;
    JLabel dateLabel = new JLabel("Date:");
    dateLabel.setFont(labelFont);
    panel.add(dateLabel, gbc);

    gbc.gridx = 3;
    JDatePickerImpl datePicker = createDatePicker(); 
    panel.add(datePicker, gbc);

    gbc.gridx = 4;
    JLabel statusLabel = new JLabel("Status:");
    statusLabel.setFont(labelFont);
    panel.add(statusLabel, gbc);

    gbc.gridx = 5;
    JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
    panel.add(statusCombo, gbc);

    // Row 9: Note Section
    gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 6;
    JLabel noteLabel = new JLabel("Note:");
    noteLabel.setFont(labelFont);
    panel.add(noteLabel, gbc);

    gbc.gridy = 10; gbc.gridx = 0;
    JTextArea noteArea = new JTextArea(3, 50);
    panel.add(new JScrollPane(noteArea), gbc);

    // Display the dialog
    int result = JOptionPane.showConfirmDialog(null, panel, "Edit Invoice", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        // Handle user inputs
        String client = clientField.getText();
        String reference = referenceField.getText();
        String paymentTerms = paymentTermsField.getText();
        String deliveryPlace = deliveryPlaceField.getText();
        String date = datePicker.getJFormattedTextField().getText();
        // Additional processing can go here
    }
}


// Custom editor for Quantity column with plus/minus buttons
class QuantityEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton plusButton, minusButton;
    private JTextField quantityField;
    private JTable table;
    private DefaultTableModel model;

    public QuantityEditor(JTable table, DefaultTableModel model) {
        this.table = table;
        this.model = model;
        panel = new JPanel(new FlowLayout());
        quantityField = new JTextField(2);
        plusButton = new JButton("+");
        minusButton = new JButton("-");

        plusButton.addActionListener(e -> incrementQuantity());
        minusButton.addActionListener(e -> decrementQuantity());

        panel.add(minusButton);
        panel.add(quantityField);
        panel.add(plusButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        quantityField.setText(value.toString());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return quantityField.getText();
    }

    private void incrementQuantity() {
        int currentQty = Integer.parseInt(quantityField.getText());
        quantityField.setText(String.valueOf(currentQty + 1));
    }

    private void decrementQuantity() {
        int currentQty = Integer.parseInt(quantityField.getText());
        if (currentQty > 0) {
            quantityField.setText(String.valueOf(currentQty - 1));
        }
    }
}

// ButtonRenderer and ButtonEditor are already defined in your previous code.



// Custom ButtonEditor class for the "Remove" button functionality
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private DefaultTableModel tableModel;

    public ButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel model) {
        super(checkBox);
        this.table = table;
        this.tableModel = model;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "Remove" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Remove the row when the button is clicked
            int row = table.getSelectedRow();
            if (row >= 0) {
                tableModel.removeRow(row);
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

            @Override
          public void onDelete(int row) {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }

            // Confirm before deleting
            int confirmation = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this Product?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(row);
                JOptionPane.showMessageDialog(null, "Product Deleted Successfully", 
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        }

            @Override

public void onView(int row) {
          Application.showForm(new Invoice_Details());

}


        };
     table.getColumnModel().getColumn(12).setCellRenderer(new TableActionCellRender());
     table.getColumnModel().getColumn(12).setCellEditor(new TableActionCellEditor(event));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Invoice");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("List of Invoice");

        jButton1.setBackground(new java.awt.Color(235, 161, 132));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Create");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setText("Search");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Invoice No	", "Invoice Date	", "Client	", "Subtotal	", "Transport", "Discount", "Tax	", "Net Total	", "Total Paid", "Total Due", "Status", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setRowHeight(40);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setResizable(false);
            table.getColumnModel().getColumn(5).setResizable(false);
            table.getColumnModel().getColumn(6).setResizable(false);
            table.getColumnModel().getColumn(7).setResizable(false);
            table.getColumnModel().getColumn(8).setResizable(false);
            table.getColumnModel().getColumn(9).setResizable(false);
            table.getColumnModel().getColumn(10).setResizable(false);
            table.getColumnModel().getColumn(11).setResizable(false);
        }

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setText("From - To");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 1272, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);  // Padding around components
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    Font labelFont = new Font("Arial", Font.BOLD, 14);  // Larger font for labels
    Dimension fieldSize = new Dimension(250, 25);  // Field size
    Dimension largeFieldSize = new Dimension(500, 25);  // For full-width fields

    // 1st Row: Client and Reference
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    JLabel clientLabel = new JLabel("Client *:");
    clientLabel.setFont(labelFont);
    panel.add(clientLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 1;
    JComboBox<String> clientCombo = new JComboBox<>(new String[]{"Walking Customer", "Customer 1", "Customer 2"});
    clientCombo.setPreferredSize(fieldSize);
    panel.add(clientCombo, gbc);

    gbc.gridx = 2;
    JLabel referenceLabel = new JLabel("Reference:");
    referenceLabel.setFont(labelFont);
    panel.add(referenceLabel, gbc);

    gbc.gridx = 3;
    JTextField referenceField = new JTextField(20);
    referenceField.setPreferredSize(fieldSize);
    panel.add(referenceField, gbc);

    // 2nd Row: Select Products
    gbc.gridx = 0;
    gbc.gridy = 1;
    JLabel productsLabel = new JLabel("Select Products *:");
    productsLabel.setFont(labelFont);
    panel.add(productsLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 3;
    JComboBox<String> productsCombo = new JComboBox<>(new String[]{"Search Products", "Plywood", "Paint", "PVC", "Hardware"});
    productsCombo.setPreferredSize(largeFieldSize);
    panel.add(productsCombo, gbc);

    // 3rd Row: Discount Type, Discount, Transport Cost
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    JLabel discountTypeLabel = new JLabel("Discount Type:");
    discountTypeLabel.setFont(labelFont);
    panel.add(discountTypeLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 1;
    JComboBox<String> discountTypeCombo = new JComboBox<>(new String[]{"Fixed", "Percentage"});
    discountTypeCombo.setPreferredSize(fieldSize);
    panel.add(discountTypeCombo, gbc);

    gbc.gridx = 2;
    JLabel discountLabel = new JLabel("Discount:");
    discountLabel.setFont(labelFont);
    panel.add(discountLabel, gbc);

    gbc.gridx = 3;
    JSpinner discountSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5));
    discountSpinner.setPreferredSize(fieldSize);
    panel.add(discountSpinner, gbc);

    // 4th Row: Transport Cost
    gbc.gridx = 0;
    gbc.gridy = 3;
    JLabel transportCostLabel = new JLabel("Transport Cost:");
    transportCostLabel.setFont(labelFont);
    panel.add(transportCostLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 1;
    JSpinner transportCostSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 100.0));
    transportCostSpinner.setPreferredSize(fieldSize);
    panel.add(transportCostSpinner, gbc);

    gbc.gridx = 2;
    JLabel taxLabel = new JLabel("Invoice Tax *:");
    taxLabel.setFont(labelFont);
    panel.add(taxLabel, gbc);

    gbc.gridx = 3;
    JComboBox<String> taxCombo = new JComboBox<>(new String[]{"VAT@0", "VAT@10", "VAT@20"});
    taxCombo.setPreferredSize(fieldSize);
    panel.add(taxCombo, gbc);

    // 5th Row: Total Tax, Net Total
    gbc.gridx = 0;
    gbc.gridy = 4;
    JLabel totalTaxLabel = new JLabel("Total Tax:");
    totalTaxLabel.setFont(labelFont);
    panel.add(totalTaxLabel, gbc);

    gbc.gridx = 1;
    JTextField totalTaxField = new JTextField(10);
    totalTaxField.setPreferredSize(fieldSize);
    totalTaxField.setEditable(false);
    panel.add(totalTaxField, gbc);

    gbc.gridx = 2;
    JLabel netTotalLabel = new JLabel("Net Total:");
    netTotalLabel.setFont(labelFont);
    panel.add(netTotalLabel, gbc);

    gbc.gridx = 3;
    JTextField netTotalField = new JTextField(10);
    netTotalField.setPreferredSize(fieldSize);
    netTotalField.setEditable(false);
    panel.add(netTotalField, gbc);

    // 6th Row: PO Reference, Payment Terms
    gbc.gridx = 0;
    gbc.gridy = 5;
    JLabel poReferenceLabel = new JLabel("PO Reference:");
    poReferenceLabel.setFont(labelFont);
    panel.add(poReferenceLabel, gbc);

    gbc.gridx = 1;
    JTextField poReferenceField = new JTextField(20);
    poReferenceField.setPreferredSize(fieldSize);
    panel.add(poReferenceField, gbc);

    gbc.gridx = 2;
    JLabel paymentTermsLabel = new JLabel("Payment Terms:");
    paymentTermsLabel.setFont(labelFont);
    panel.add(paymentTermsLabel, gbc);

    gbc.gridx = 3;
    JTextField paymentTermsField = new JTextField(20);
    paymentTermsField.setPreferredSize(fieldSize);
    panel.add(paymentTermsField, gbc);

    // 7th Row: Delivery Place, Date, Status
    gbc.gridx = 0;
    gbc.gridy = 6;
    JLabel deliveryPlaceLabel = new JLabel("Delivery Place:");
    deliveryPlaceLabel.setFont(labelFont);
    panel.add(deliveryPlaceLabel, gbc);

    gbc.gridx = 1;
    JTextField deliveryPlaceField = new JTextField(20);
    deliveryPlaceField.setPreferredSize(fieldSize);
    panel.add(deliveryPlaceField, gbc);

    gbc.gridx = 2;
    JLabel dateLabel = new JLabel("Date:");
    dateLabel.setFont(labelFont);
    panel.add(dateLabel, gbc);

    gbc.gridx = 3;
    JDatePickerImpl datePicker = createDatePicker();
    datePicker.setPreferredSize(fieldSize);
    panel.add(datePicker, gbc);

    // 8th Row: Status
    gbc.gridx = 0;
    gbc.gridy = 7;
    JLabel statusLabel = new JLabel("Status:");
    statusLabel.setFont(labelFont);
    panel.add(statusLabel, gbc);

    gbc.gridx = 1;
    JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
    statusCombo.setPreferredSize(fieldSize);
    panel.add(statusCombo, gbc);

    // 9th Row: Note
    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 1;
    JLabel noteLabel = new JLabel("Note:");
    noteLabel.setFont(labelFont);
    panel.add(noteLabel, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 3;
    JTextArea noteArea = new JTextArea(3, 50);
    noteArea.setPreferredSize(largeFieldSize);
    panel.add(new JScrollPane(noteArea), gbc);

    // Display the panel
    int result = JOptionPane.showConfirmDialog(null, panel, "Create Quotation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        // Submission logic
        String client = (String) clientCombo.getSelectedItem();
        String reference = referenceField.getText();
        String product = (String) productsCombo.getSelectedItem();
        String discountType = (String) discountTypeCombo.getSelectedItem();
        Double discount = (Double) discountSpinner.getValue();
        Double transportCost = (Double) transportCostSpinner.getValue();
        String quotationTax = (String) taxCombo.getSelectedItem();
        String totalTax = totalTaxField.getText();
        String netTotal = netTotalField.getText();
        String poReference = poReferenceField.getText();
        String paymentTerms = paymentTermsField.getText();
        String deliveryPlace = deliveryPlaceField.getText();
        String note = noteArea.getText();
    }
    }


// Method to create date pickers with the current date
private JDatePickerImpl createDatePicker() {
    UtilDateModel model = new UtilDateModel();
    LocalDate currentDate = LocalDate.now();
    model.setDate(currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());
    model.setSelected(true);
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");
    JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
    return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Create a panel to hold the date pickers for "From" and "To" dates
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 10, 10));  // GridLayout with 2 rows, 2 columns

        // Create bold and larger font for the labels
        Font labelFont = new Font("Arial", Font.BOLD, 16);  // 16 size and bold

        JLabel fromLabel = new JLabel("From Date:");
        fromLabel.setFont(labelFont);  // Set to bold and larger size

        JLabel toLabel = new JLabel("To Date:");
        toLabel.setFont(labelFont);  // Set to bold and larger size

        // Create the date pickers
        JDatePickerImpl fromDatePicker = createDatePicker();  // Date picker for "From" date
        JDatePickerImpl toDatePicker = createDatePicker();    // Date picker for "To" date

        // Add components to the panel
        datePanel.add(fromLabel);
        datePanel.add(fromDatePicker);
        datePanel.add(toLabel);
        datePanel.add(toDatePicker);

        // Show a dialog with the date pickers
        int result = JOptionPane.showConfirmDialog(null, datePanel, "Select Date Range", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Retrieve the selected dates
            String fromDateStr = fromDatePicker.getJFormattedTextField().getText();
            String toDateStr = toDatePicker.getJFormattedTextField().getText();

            // Parse the dates into a Date object or LocalDate for comparison
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");  // Adjust format if necessary
            LocalDate fromDate = LocalDate.parse(fromDateStr, formatter);
            LocalDate toDate = LocalDate.parse(toDateStr, formatter);

            // Now, filter the table rows based on the selected date range
            filterTableByDateRange(fromDate, toDate);
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

 private void filterTableByDateRange(LocalDate fromDate, LocalDate toDate) {
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
    table.setRowSorter(sorter);

    sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
        @Override
        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
            // Assuming the date is in the 3rd column (index 2), change as per your table
            String dateStr = (String) entry.getValue(2);
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate rowDate = LocalDate.parse(dateStr, formatter);

                // Return true if the date is within the selected range
                return !rowDate.isBefore(fromDate) && !rowDate.isAfter(toDate);
            } catch (Exception e) {
                // Skip rows with invalid dates
                return false;
            }
        }
    });
 }
 }
