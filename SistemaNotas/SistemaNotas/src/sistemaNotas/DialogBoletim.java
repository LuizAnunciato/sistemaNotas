package sistemaNotas;

import java.awt.BorderLayout;
import java.awt.Font;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DialogBoletim extends JDialog {

    public DialogBoletim(
            JFrame proprietario,
            Aluno aluno,
            List<ResultadoNota> notas) {

        super(
            proprietario,
            "Boletim do Aluno",
            true
        );

        setSize(700, 450);
        setLocationRelativeTo(proprietario);

        JPanel topo = new JPanel(
                new BorderLayout());

        topo.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15));

        JLabel titulo =
                new JLabel(
                    "Boletim - "
                    + aluno.getNome()
                    + " (" + aluno.getRa() + ")"
                );

        titulo.setFont(
                titulo.getFont()
                      .deriveFont(
                            Font.BOLD,
                            18f));

        topo.add(
                titulo,
                BorderLayout.CENTER
        );

        String[] colunas = {
                "Disciplina",
                "Prova",
                "Nota"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(
                        colunas, 0) {

            private static final long
                    serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(
                    int row, int column) {

                return false;
            }
        };

        for (ResultadoNota nota : notas) {

            modelo.addRow(
                new Object[] {

                    nota.getDisciplina(),
                    nota.getProva(),

                    String.format(
                            "%.2f",
                            nota.getNota()
                                .doubleValue())
                }
            );
        }

        JTable tabela = new JTable(modelo);

        tabela.setRowHeight(25);

        JScrollPane scroll =
                new JScrollPane(tabela);

        JButton btnFechar =
                new JButton("Fechar");

        btnFechar.addActionListener(
                e -> dispose()
        );

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnFechar, BorderLayout.SOUTH);
    }
}