
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import database.Config;
import database.Query;

import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;

public class UpdateDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		try {
	//			UpdateDialog dialog = new UpdateDialog();
	//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	//			dialog.setVisible(true);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

	/**
	 * Create the dialog.
	 */
	private JFormattedTextField formattedTextField;
	private JLabel lblError;
	public UpdateDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 226, 139);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Set Infection Rate(%) : ");
			lblNewLabel.setBounds(26, 41, 117, 14);
			contentPanel.add(lblNewLabel);
		}

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Double.class);
		//formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(true);

		formattedTextField = new JFormattedTextField(formatter);
		formattedTextField.setBounds(148, 38, 46, 20);
		formattedTextField.setColumns(5);
		formattedTextField.setText(""+Config.getInfectionRate());
		contentPanel.add(formattedTextField);

		lblError = new JLabel("");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setForeground(Color.RED);
		lblError.setBounds(10, 11, 200, 14);
		contentPanel.add(lblError);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("Save");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double rate = new Double(formattedTextField.getText());
				if(rate > 0.2 || rate < -1)
				{
					lblError.setText("Enter valid rate beteen -1 to 0.2");
				}
				else
				{
					lblError.setText("");
					try {
						Query.setInfectionRate(rate);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Config.setInfectionRate(rate);
					UpdateDialog.this.dispose();
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateDialog.this.dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		this.setLocationRelativeTo(null);
	}
}
