package br.com.luis.receituario;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

public class Principal {

	private JFrame frmReceiturio;
	private JTextField txtUniSaude;
	private JTextField txtPaciente;
	private List<Receita> lista;
	private JTextField txtIdade;
	private JTextPane txtReceita = new JTextPane();
	private JTextPane txtReqExa = new JTextPane();
	private JCheckBox chkControleEspecial = new JCheckBox("Controle Especial");
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	DefaultComboBoxModel cboSexoModel = new DefaultComboBoxModel(new String[] {"","Femenino", "Masculino"});
	@SuppressWarnings({ "rawtypes", "unchecked" })
	DefaultComboBoxModel cboTipImpModel = new DefaultComboBoxModel(new String[] {"2 por página", "1 por página"});
	@SuppressWarnings({ "rawtypes", "unchecked" })
	DefaultComboBoxModel cboCidTraModel = new DefaultComboBoxModel(new String[] {"Rio Negro","Itaiópolis", "Mafra"});
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LookAndFeel visual = new LookAndFeel();
					visual.setLookAndFeel(1);

					Principal window = new Principal();
					window.frmReceiturio.setVisible(true);									
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private void imprimirReceita(){
		String caminhoRel =  "Relatorios/";				
		String caminhoImg = "Imagens/";										
		String relatorio = "";
		
		Date data = new Date();
		SimpleDateFormat datFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Receita receita = new Receita();
		
		if(cboCidTraModel.getSelectedItem() == "Rio Negro"){
			receita.setImagem(caminhoImg + "brasao_rionegro.png");
			receita.setCabecalho01("SECRETARIA MUNICIPAL DE RIO NEGRO");
			receita.setCabecalho02("CNPJ 76.002.641/0001-47 - Fone(47) 3642-3280/3642-1727");
			receita.setCabecalho03("Rua Juvenal Ferreira Pinto, nº 2070 - Seminário");
			receita.setCabecalho04("CEP 83.880-000-Rio Negro-PR");			
		}
		if(cboCidTraModel.getSelectedItem() == "Itaiópolis"){
			receita.setImagem(caminhoImg + "brasao_itaiopolis.png");						
			receita.setCabecalho01("SECRETARIA MUNICIPAL DE ITAIÓPOLIS");
			receita.setCabecalho02("CNPJ 83.102.517/0001-19 -  Fone(47) 3652-2211");
			receita.setCabecalho03("Avenida Dr. Getúlio Vargas, nº 308 - Centro");
			receita.setCabecalho04("CEP 89340-000-Itaiópolis-SC");
		}
		if(cboCidTraModel.getSelectedItem() == "Mafra"){
			receita.setImagem(caminhoImg + "brasao_mafra.png");			
			receita.setCabecalho01("SECRETARIA MUNICIPAL DE MAFRA");
			receita.setCabecalho02("CNPJ 83.102.509/0001-72 - Fone(47) 3641-4000");
			receita.setCabecalho03("Pça. Desembargador Flávio Tavares, nº 12 - Centro");
			receita.setCabecalho04("CEP: 89.300-000-Mafra-SC");
		}

		receita.setUnidadeSaude(txtUniSaude.getText());
		receita.setPaciente(txtPaciente.getText());
		receita.setIdade(txtIdade.getText());
		receita.setSexo(cboSexoModel.getSelectedItem().toString());		
		receita.setData(datFormat.format(data));
				
		String txtStrReceita = txtReceita.getText().trim() ;
		String txtStrReqReceita = txtReqExa.getText().trim();
			
		receita.setTitulo("Receituáio");
		receita.setTituloReqExa("");
		receita.setIndicacaoExames("");		

		receita.setDescricaoReceita(txtStrReceita);
		
		if(!chkControleEspecial.isSelected()){
			if (!txtStrReqReceita.isEmpty() && txtStrReceita.isEmpty()){
				receita.setTitulo("Requisição de Exames");			
				receita.setTituloReqExa("Procedimentos Solicitados");
				receita.setIndicacaoExames("");
				receita.setDescricaoReceita(txtStrReqReceita);
			}
		}
		
		lista = new ArrayList<>();
		lista.add(receita);
		
		if(chkControleEspecial.isSelected()){
			if (cboTipImpModel.getSelectedItem() == "1 por página") relatorio = "ReceituarioControlado1-1.jrxml";
			if (cboTipImpModel.getSelectedItem() == "2 por página") relatorio = "ReceituarioControlado2-1.jrxml";		
		}else{
			if (cboTipImpModel.getSelectedItem() == "1 por página") relatorio = "Receituario1-1.jrxml";
			if (cboTipImpModel.getSelectedItem() == "2 por página") relatorio = "Receituario2-1.jrxml";
		}		
						
		chamarJasper(caminhoRel +  relatorio);
		
		lista.clear();		
	}
	
	private void limparTela(){		
		txtPaciente.setText("");
		txtIdade.setText("");
		txtReceita.setText("");
		txtReqExa.setText("");
		cboSexoModel.setSelectedItem("");		
	}
	
	
	private void chamarJasper(String caminho){
		try {
			JasperReport report = JasperCompileManager.compileReport(caminho);
			JasperPrint print = JasperFillManager.fillReport(report, null,new JRBeanCollectionDataSource(lista));
					
			JasperViewer view = new JasperViewer(print,false);
			
			view.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
			view.setVisible(true);
			
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro.: " + e.getMessage(), "Receituário", 0,null);
		}
	}
	
	/**
	 * Create the application.
	 */
	public Principal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmReceiturio = new JFrame();
		frmReceiturio.setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/br/com/luis/icone/ico.png")));
		frmReceiturio.setTitle("Receitu\u00E1rio");
		frmReceiturio.setBounds(100, 100, 708, 469);
		frmReceiturio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menuBar.setBackground(Color.LIGHT_GRAY);
		frmReceiturio.setJMenuBar(menuBar);
		
		JComboBox cboTipImp = new JComboBox();
		cboTipImp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menuBar.add(cboTipImp);
		cboTipImp.setModel(cboTipImpModel);
		
		JComboBox cboCidTra = new JComboBox();
		cboCidTra.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menuBar.add(cboCidTra);
		cboCidTra.setModel(cboCidTraModel);
		
		JButton btnImprimir = new JButton("Imprimir");
		btnImprimir.setIcon(new ImageIcon(Principal.class.getResource("/br/com/luis/icone/print.png")));
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				imprimirReceita();
			}
		});
		btnImprimir.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menuBar.add(btnImprimir);
		
		JPanel panPaciente = new JPanel();
		panPaciente.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frmReceiturio.getContentPane().add(panPaciente, BorderLayout.NORTH);
		
		JLabel lblUnidadeDeSade = new JLabel("Unidade de Sa\u00FAde");
		lblUnidadeDeSade.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUnidadeDeSade.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtUniSaude = new JTextField();
		txtUniSaude.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtUniSaude.setColumns(10);
		
		JLabel lblPaciente = new JLabel("Paciente");
		lblPaciente.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPaciente.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtPaciente = new JTextField();
		txtPaciente.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtPaciente.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Idade");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblSexo = new JLabel("Sexo");
		lblSexo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limparTela();
			}
		});
		btnLimpar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtIdade = new JTextField();
		txtIdade.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtIdade.setColumns(10);
				
		JComboBox cboSexo = new JComboBox();
		cboSexo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cboSexo.setModel(cboSexoModel);
		
		GroupLayout gl_panPaciente = new GroupLayout(panPaciente);
		gl_panPaciente.setHorizontalGroup(
			gl_panPaciente.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panPaciente.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panPaciente.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblPaciente, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblUnidadeDeSade, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panPaciente.createParallelGroup(Alignment.LEADING)
						.addComponent(txtPaciente, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
						.addComponent(txtUniSaude, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
						.addGroup(gl_panPaciente.createSequentialGroup()
							.addComponent(txtIdade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblSexo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cboSexo, 0, 338, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnLimpar, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panPaciente.setVerticalGroup(
			gl_panPaciente.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panPaciente.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panPaciente.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUnidadeDeSade)
						.addComponent(txtUniSaude, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panPaciente.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPaciente)
						.addComponent(txtPaciente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panPaciente.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(lblSexo)
						.addComponent(btnLimpar)
						.addComponent(txtIdade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboSexo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		panPaciente.setLayout(gl_panPaciente);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tabbedPane.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frmReceiturio.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panReceituario = new JPanel();
		tabbedPane.addTab("Receitu\u00E1rio", null, panReceituario, null);
		
		
		chkControleEspecial.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panReceituario = new GroupLayout(panReceituario);
		gl_panReceituario.setHorizontalGroup(
			gl_panReceituario.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panReceituario.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panReceituario.createParallelGroup(Alignment.TRAILING)
						.addComponent(chkControleEspecial, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panReceituario.setVerticalGroup(
			gl_panReceituario.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panReceituario.createSequentialGroup()
					.addComponent(chkControleEspecial)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		
		txtReceita.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(txtReceita);
		panReceituario.setLayout(gl_panReceituario);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Requisi\u00E7\u00E3o de Exames", null, panel_1, null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		scrollPane_1.setViewportView(txtReqExa);
		panel_1.setLayout(gl_panel_1);
	}		
}
