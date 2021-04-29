
package gui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;

import database.BulkLoad;
import database.Config;
import database.Connector;
import database.Query;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.event.ActionEvent;


public class MainWindow {
	public MainWindow() {
		initialize();
	}
	private JFrame frmCovidSimulator;
	private static MainWindow window; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					window = new MainWindow();
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					window.frmCovidSimulator.setLocation(dim.width/2-window.frmCovidSimulator.getSize().width/2,
							dim.height/2-window.frmCovidSimulator.getSize().height/2);

					window.frmCovidSimulator.setVisible(true);
					window.setLoginState(false);
					//window.setLoginState(true);
					Query.setDefaultConfig();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	public static MainWindow getInstance() {
		return window;
	}
	JButton btnSetRate, btnGraph, btnReport, btnImportData, btnLogin, btnAddPatient, btnChangePassword;
	private JButton btnUpdatePatient;
	public void setLoginState(boolean state)
	{
		btnAddPatient.setEnabled(state);
		btnUpdatePatient.setEnabled(state);
		btnImportData.setEnabled(state);
		btnChangePassword.setEnabled(state);

		Path path;
		try {
			path = Files.createTempFile(Connector.getDBName(), ".script");
			if(Files.exists(path))
			{
				btnReport.setEnabled(true);
				btnGraph.setEnabled(true);
			}
			else
			{
				btnReport.setEnabled(false);
				btnGraph.setEnabled(false);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		if(state)
		{
			btnLogin.setText("Logout");
		}
		else
		{
			btnLogin.setText("Login");
		}
		btnSetRate.setEnabled(true);
	}
	public void setLoadedState()
	{
		Path path;
		try {
			path = Files.createTempFile(Connector.getDBName(), ".script");
			if(Files.exists(path))
			{
				btnReport.setEnabled(true);
				btnGraph.setEnabled(true);
			}
			else
			{
				btnReport.setEnabled(false);
				btnGraph.setEnabled(false);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void initialize() {
		frmCovidSimulator = new JFrame();
		frmCovidSimulator.setTitle("COVID-19 Simulator");
		frmCovidSimulator.setResizable(false);
		frmCovidSimulator.setBounds(100, 100, 560, 375);
		frmCovidSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SwingUtilities.updateComponentTreeUI(frmCovidSimulator);

		BufferedImage myPicture;
		JLabel picLabel;


		btnSetRate = new JButton("Set Infection Rate");
		btnSetRate.setBounds(397, 45, 147, 23);
		btnSetRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				UpdateDialog dialog = new UpdateDialog();
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});

		btnGraph = new JButton("View Graph");
		btnGraph.setEnabled(false);
		btnGraph.setBounds(457, 11, 87, 23);
		btnGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DateRangeSelector dateView = new DateRangeSelector();
					dateView.setModalityType(ModalityType.APPLICATION_MODAL);
					dateView.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dateView.setLaunchMode(true);
					dateView.setVisible(true);
					//LineChart.draw();
					//new Report().displayReport();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		frmCovidSimulator.getContentPane().setLayout(null);
		frmCovidSimulator.getContentPane().add(btnGraph);

		btnReport = new JButton("View Report");
		btnReport.setEnabled(false);
		btnReport.setBounds(356, 11, 91, 23);
		btnReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateRangeSelector dateView = new DateRangeSelector();
				dateView.setModalityType(ModalityType.APPLICATION_MODAL);
				dateView.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dateView.setVisible(true);
				//				try {
				//					new Report().displayReport();
				//				} catch (Exception e1) {
				//					// TODO Auto-generated catch block
				//					e1.printStackTrace();
				//					textArea.setText(textArea.getText() +"\n"+e1.getMessage());
				//				}
			}
		});
		frmCovidSimulator.getContentPane().add(btnReport);
		frmCovidSimulator.getContentPane().add(btnSetRate);

		btnImportData = new JButton("Import Data");
		btnImportData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV File","csv","CSV");
				fileChooser.setFileFilter(filter);

				int result = fileChooser.showOpenDialog(frmCovidSimulator);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						BulkLoad.insertWorldDataFromCSV(selectedFile.getAbsolutePath());
						BulkLoad.insertRandomPatientDataFromWorldCSV(selectedFile.getAbsolutePath());
						btnReport.setEnabled(true);
						btnGraph.setEnabled(true);
						ConfirmPopup popup = new ConfirmPopup("Imported successfully", Color.BLACK);
						popup.setModalityType(ModalityType.APPLICATION_MODAL);
						popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						popup.setVisible(true);
					}
					catch (Exception e1) {
						// TODO Auto-generated catch block
						ConfirmPopup popup = new ConfirmPopup("Import failed", Color.RED);
						popup.setModalityType(ModalityType.APPLICATION_MODAL);
						popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						popup.setVisible(true);
						e1.printStackTrace();
					}
				}
			}
		});
		btnImportData.setBounds(12, 244, 105, 23);
		frmCovidSimulator.getContentPane().add(btnImportData);

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnLogin.getText().equals("Login"))
				{
					LoginPopup loginPopup = new LoginPopup();
					loginPopup.setModalityType(ModalityType.APPLICATION_MODAL);
					loginPopup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					loginPopup.setVisible(true);
				}
				else
				{
					setLoginState(false);
				}
			}
		});
		btnLogin.setBounds(12, 176, 89, 23);
		frmCovidSimulator.getContentPane().add(btnLogin);

		btnAddPatient = new JButton("Add Patient Info");
		btnAddPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PatientInfoView view = new PatientInfoView();
				view.setModalityType(ModalityType.APPLICATION_MODAL);
				view.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				view.setVisible(true);
			}
		});
		btnAddPatient.setBounds(12, 278, 137, 23);
		frmCovidSimulator.getContentPane().add(btnAddPatient);

		btnUpdatePatient = new JButton("Update Patient Info");
		btnUpdatePatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdatePatientInfo view = new UpdatePatientInfo();
				view.setModalityType(ModalityType.APPLICATION_MODAL);
				view.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				view.setVisible(true);
			}
		});
		btnUpdatePatient.setEnabled(false);
		btnUpdatePatient.setBounds(12, 312, 137, 23);
		frmCovidSimulator.getContentPane().add(btnUpdatePatient);

		btnChangePassword = new JButton("Change password");
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginPopup loginPopup = new LoginPopup();
				loginPopup.setModalityType(ModalityType.APPLICATION_MODAL);
				loginPopup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				loginPopup.launchToUpdatePassword();
				loginPopup.setVisible(true);
			}
		});
		btnChangePassword.setBounds(12, 210, 127, 23);
		frmCovidSimulator.getContentPane().add(btnChangePassword);
		
		try {
			//myPicture = ImageIO.read(this.getClass().getResource("model/background.png"));
			myPicture = ImageIO.read(new File("model/background.png"));
			picLabel = new JLabel(new ImageIcon(myPicture));
			picLabel.setLocation(0, 0);
			picLabel.setSize(554,346);
			frmCovidSimulator.getContentPane().add(picLabel);

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			Config.setInfectionRate(Query.getInfectionRate());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setLoginState(false);
		setLoadedState();
	}
}
